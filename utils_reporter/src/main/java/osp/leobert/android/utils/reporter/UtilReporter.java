package osp.leobert.android.utils.reporter;

import com.google.auto.service.AutoService;

import net.steppschuh.markdowngenerator.list.ListBuilder;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.list.UnorderedListItem;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import net.steppschuh.markdowngenerator.text.quote.Quote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import osp.leobert.android.reportprinter.spi.Model;
import osp.leobert.android.reportprinter.spi.ReporterExtension;
import osp.leobert.android.reportprinter.spi.Result;

/**
 * <p><b>Package:</b> osp.leobert.android.utils.reporter </p>
 * <p><b>Project:</b> MotorFans </p>
 * <p><b>Classname:</b> UtilReporter </p>
 *
 * Created by leobert on 2019/2/13.
 */
@AutoService(ReporterExtension.class)
public class UtilReporter implements ReporterExtension {

    public static class Foo {
        Model model;

        Util util;

        Foo(Model model, Util util) {
            this.model = model;
            this.util = util;
        }

        public static Foo create(Model model, Util util) {
            return new Foo(model, util);
        }
    }

    private String getDay() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat timeStyle = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
        return timeStyle.format(date);
    }

    @Override
    public Set<String> applicableAnnotations() {
        return Collections.singleton(Util.class.getName());
    }

    private final Map<String, List<Foo>> groupedByUsage = new LinkedHashMap<>();
    private final List<String> viewHolderDocBlocks = new ArrayList<>();
    private final String END = "\n";
    private final String RETURN = "\r\n";

    @Override
    public Result generateReport(Map<String, List<Model>> previousData) {
        if (previousData == null)
            return null;

        List<Model> UtilModels = previousData.get(Util.class.getName());
        if (UtilModels == null || UtilModels.isEmpty())
            return Result.newBuilder().handled(false).build();
        StringBuilder docBuilder = new StringBuilder();

        for (Model model : UtilModels) {
            Util annotation = model.getElement().getAnnotation(Util.class);
            groupBy(model, annotation);
            generateDocBlock(model, annotation);
        }

        docBuilder.append(new Heading("Utils")).append(END);


        docBuilder.append(new BoldText(getDay())).append(END);

        docBuilder.append(new Heading("Util Groups", 2)).append(END);

        //All Groups
        docBuilder.append(new BoldText("All Groups:")).append(END).append(RETURN);
        UnorderedList<UnorderedListItem> allGroupsList = new UnorderedList<>();
        Set<String> groups = groupedByUsage.keySet();
        final List<UnorderedListItem> allGroupsListItems = new ArrayList<>();


        for (String group : groups) {
            allGroupsListItems.add(new UnorderedListItem(group));
        }
        allGroupsList.setItems(allGroupsListItems);
        docBuilder.append(allGroupsList).append(RETURN).append(RETURN);


        //Each Util used in each group
        UnorderedList eachGroupsDetail;
        ListBuilder listBuilder = new ListBuilder();
        docBuilder.append(new BoldText("Usage Details")).append(END).append(RETURN);


        for (String group : groups) {
            listBuilder.append(group);
            final ListBuilder eachGroupBuilder = new ListBuilder();

            List<Foo> foos = groupedByUsage.get(group);
            for (Foo foo : foos) {
                eachGroupBuilder.append(getSimpleInfo(foo.model, foo.util));
            }
            listBuilder.append(eachGroupBuilder);
        }

        eachGroupsDetail = listBuilder.build();
        docBuilder.append(eachGroupsDetail).append(new Text("\r\n\r\n"));


        docBuilder.append(new Heading("Util Detail", 2)).append(END).append(RETURN);

        for (String docBlock : viewHolderDocBlocks) {
            docBuilder.append(docBlock).append(RETURN);
        }

        return Result.newBuilder()
                .handled(true)
                .reportFileNamePrefix("Utils")
                .fileExt("md")
                .reportContent(docBuilder.toString())
                .build();
    }

    private void groupBy(Model model, Util notation) {
        String groupKey = String.valueOf(notation.group());
        if (groupedByUsage.containsKey(groupKey)) {
            groupedByUsage.get(groupKey).add(Foo.create(model, notation));
        } else {
            List<Foo> list = new ArrayList<>();
            list.add(Foo.create(model, notation));
            groupedByUsage.put(groupKey, list);
        }

    }

    /**
     * ### {Util name}
     * SupportVersion: `1` , `2` , ...
     * Used At:
     */
    private void generateDocBlock(Model model, Util notation) {
        String usage = notation.usage();

        StringBuilder docBlockBuilder = new StringBuilder();

//        //header
//        String alias = getAlias(model, notation);
//        docBlockBuilder.append(new Heading(alias, 3)).append(END);

        docBlockBuilder.append(new Quote("MethodPath: " + model.getName())).append(END);
        docBlockBuilder.append(RETURN);

        docBlockBuilder.append(END).append(RETURN);

        //Used At
        docBlockBuilder.append(new Text("Usage: "));
        docBlockBuilder.append(new BoldText(usage))
                .append(new Text("."));
        docBlockBuilder.append(END).append(RETURN);

        docBlockBuilder.append(RETURN);

        viewHolderDocBlocks.add(docBlockBuilder.toString());
    }

    private String getSimpleInfo(Model model, Util util) {

        String name = model.getName();
        if (name.contains(".")) {
            int index = name.lastIndexOf("#");
            name = name.substring(index + 1);
        }

        String usage = util.usage();
        if (/*usage !=  null && */usage.length() > 10) {
            usage = usage.substring(0, 10) + "...";
        }
        if ("".equals(usage)) {
            return name;
        } else {
            if (usage.contains(name))
                return usage;
            return usage + " " + name;
        }
    }
}
