package au.edu.unimelb.csse.trollsvsgoats.core.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import playn.core.Font;
import playn.core.PlayN;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.model.Badge;
import react.Function;
import react.UnitSlot;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.ClickableTextWidget;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.Slider;
import tripleplay.ui.Style;
import tripleplay.ui.ValueLabel;
import tripleplay.ui.layout.AxisLayout;

//this is a test class to check controls
public class HelpScreenEx extends View {
	@Override
	public String[] images() {
        return new String[]{"smiley","bg_level"};
	}

	public HelpScreenEx(TrollsVsGoatsGame game) {
		super(game);
	}

	@Override
	protected Group createIface() {
		root.addStyles(Style.BACKGROUND.is(Background
                .image(getImage("bg_level"))));
		topPanel.addStyles(Style.BACKGROUND.is(Background.blank()));
		
		Group iface = new Group(AxisLayout.vertical().gap(10)).add(sliderAndLabel(
                new Slider(0, -50, 50).addStyles(
                        Slider.THUMB_IMAGE.is(getImage("smiley")),
                        Slider.BAR_HEIGHT.is(5f),
                        Slider.BAR_BACKGROUND.is(Background.roundRect(0xFFFF0000, 2.5f))), "-00"));

            return iface;
	}
	
	protected Group sliderAndLabel (Slider slider, String minText) {
        ValueLabel label = new ValueLabel(slider.value.map(FORMATTER)).
            setStyles(Style.HALIGN.right, Style.FONT.is(FIXED)).
            setConstraint(Constraints.minSize(minText));
        return new Group(AxisLayout.horizontal()).add(slider, label);
    }
	
	protected Function<Float,String> FORMATTER = new Function<Float,String>() {
        public String apply (Float value) {
            return String.valueOf(value.intValue());
        }
    };
    
    protected static Font FIXED = PlayN.graphics().createFont("Fixed", Font.Style.PLAIN, 16);

	@Override
	protected String title() {
		
		return "Help";
	}

}
