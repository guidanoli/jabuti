package gui.defaults;

import java.awt.*;

/**
 * <p>Auto-adjusting Card Layout
 * <p>From <a href="https://stackoverflow.com/a/23881790/5696107">StackOverflow</a>
 * @author MadProgrammer
 *
 */
@SuppressWarnings("serial")
public class DefaultCardLayout extends CardLayout {

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Component current = findCurrentComponent(parent);
        if (current != null) {
            Insets insets = parent.getInsets();
            Dimension pref = current.getPreferredSize();
            pref.width += insets.left + insets.right;
            pref.height += insets.top + insets.bottom;
            return pref;
        }
        return super.preferredLayoutSize(parent);
    }

    public Component findCurrentComponent(Container parent) {
        for (Component comp : parent.getComponents()) {
            if (comp.isVisible()) {
                return comp;
            }
        }
        return null;
    }
}