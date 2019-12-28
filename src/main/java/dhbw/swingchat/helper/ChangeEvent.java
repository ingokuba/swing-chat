package dhbw.swingchat.helper;

public class ChangeEvent
{

    private ChangeMode mode;
    private Object     object;

    private ChangeEvent(ChangeMode mode, Object object)
    {
        this.mode = mode;
        this.object = object;
    }

    public static ChangeEvent add(Object object)
    {
        return new ChangeEvent(ChangeMode.ADD, object);
    }

    public static ChangeEvent remove(Object object)
    {
        return new ChangeEvent(ChangeMode.REMOVE, object);
    }

    public ChangeMode getMode()
    {
        return mode;
    }

    public Object getObject()
    {
        return object;
    }
}
