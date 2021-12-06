package onelemonyboi.lemonlib.trait;

public interface ICanCopy {
    default public Object deepCopy() {
        return this;
    }
}
