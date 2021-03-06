package com.jade;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import com.dataStructure.Transform;
import com.file.Parser;
import com.file.Serialize;

public class GameObject extends Serialize {
    private List<Component> components;
    private String name;
    public Transform transform;
    public boolean serializable = true, isUi = false;
    public int zIndex;

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component cmp : components) {
            if (componentClass.isAssignableFrom(cmp.getClass())) {
                try {
                    return componentClass.cast(cmp);
                } catch (ClassCastException classCastExc) {
                	classCastExc.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (Component cmp : components) {
            if (componentClass.isAssignableFrom(cmp.getClass())) {
                components.remove(cmp);
                return;
            }
        }
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void addComponent(Component c) {
        components.add(c);
        c.gameObject = this;
    }

    public GameObject copy() {
        GameObject newGameObject = new GameObject("Generated", transform.copy(), this.zIndex);
        for (Component cmp : components) {
            Component copy = cmp.copy();
            if (copy != null) {
                newGameObject.addComponent(copy);
            }
        }
        return newGameObject;
    }


    public void update(double up) {
        for (Component c : components) {
            c.update(up);
        }
    }

    public void setNonserializable() {
        serializable = false;
    }

    public void draw(Graphics2D graph2) {
        for (Component cmp : components) {
            cmp.draw(graph2);
        }
    }

    @Override
    public String serialize(int tabSize) {
        if (!serializable) {
        	return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(beginObjectProperty("GameObject", tabSize));
        builder.append(transform.serialize(tabSize + 1));
        builder.append(addEnding(true, true));
        builder.append(addStringProperty("Name", name, tabSize + 1, true, true));

        if (components.size() > 0) {
            builder.append(addIntProperty("ZIndex", this.zIndex, tabSize + 1, true, true));
            builder.append(beginObjectProperty("Components", tabSize + 1));
        } else {
            builder.append(addIntProperty("ZIndex", this.zIndex, tabSize + 1, true, false));
        }

        int i = 0;
        for (Component cmp : components) {
            String str = cmp.serialize(tabSize + 2);
            if (str.compareTo("") != 0) {
                builder.append(str);
                if (i != components.size() - 1) {
                    builder.append(addEnding(true, true));
                } else {
                    builder.append(addEnding(true, false));
                }
            }
            i++;
        }
        if (components.size() > 0) {
            builder.append(closeObjectProperty(tabSize + 1));
        }
        builder.append(addEnding(true, false));
        builder.append(closeObjectProperty(tabSize));
        return builder.toString();
    }

    public static GameObject deserialize() {
        Parser.consumeBeginObjectProperty("GameObject");
        Transform transform = Transform.deserialize();
        Parser.consume(',');
        String name = Parser.consumeStringProperty("Name");
        Parser.consume(',');
        int zIndex = Parser.consumeIntProperty("ZIndex");
        GameObject go = new GameObject(name, transform, zIndex);

        if (Parser.peek() == ',') {
            Parser.consume(',');
            Parser.consumeBeginObjectProperty("Components");
            go.addComponent(Parser.parseComponent());
            while (Parser.peek() == ',') {
                Parser.consume(',');
                go.addComponent(Parser.parseComponent());
            }
            Parser.consumeEndObjectProperty();
        }
        Parser.consumeEndObjectProperty();
        return go;
    }

    public void setUi(boolean set) {
        this.isUi = set;
    }
}
