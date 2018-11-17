package osp.leobert.android.reportprinter.spi;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * <p><b>Package:</b> osp.leobert.android.reportprinter.spi </p>
 * <p><b>Project:</b> ReportPrinter </p>
 * <p><b>Classname:</b> Model </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/11/17.
 */
public class Model {
    Element element;
    String name;
    ElementKind elementKind;
    TypeElement annoType;

    public Element getElement() {
        return element;
    }

    public String getName() {
        return name;
    }

    public ElementKind getElementKind() {
        return elementKind;
    }

    public TypeElement getAnnoType() {
        return annoType;
    }

    private Model(Builder builder) {
        element = builder.element;
        name = builder.name;
        elementKind = builder.elementKind;
        annoType = builder.annoType;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private Element element;
        private String name;
        private ElementKind elementKind;
        private TypeElement annoType;

        private Builder() {
        }

        public Builder element(Element val) {
            element = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder elementKind(ElementKind val) {
            elementKind = val;
            return this;
        }

        public Builder annoType(TypeElement val) {
            annoType = val;
            return this;
        }

        public Model build() {
            return new Model(this);
        }
    }
}
