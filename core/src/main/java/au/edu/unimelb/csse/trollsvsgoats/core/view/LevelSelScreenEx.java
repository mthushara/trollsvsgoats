package au.edu.unimelb.csse.trollsvsgoats.core.view;

import static tripleplay.ui.layout.TableLayout.COL;

import java.util.ArrayList;

import playn.core.Font;
import playn.core.Image;
import playn.core.Key;
import playn.core.Mouse;
import playn.core.PlayN;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import pythagoras.f.IDimension;
import react.UnitSlot;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.model.Badge;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.Icon;
import tripleplay.ui.Label;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.Style.Binding;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;

public class LevelSelScreenEx extends View {
	private int score = 0;
	
	private final static int TILE_WIDTH = 400;
    
    Icon iconChildBoard = null;
    private final int Y_START_POS = -22;
    public final static int ICON_WIDTH = 60;
    public final static int DESCRIPTION_WIDTH = TILE_WIDTH - ICON_WIDTH;
    private final static int SCROLL_HEIGHT = 300;
    private final static int BUTTON_SCROLL_DISTANCE = 10;
    final Binding<Background> selOn = Style.BACKGROUND.is(Background.blank()
            .inset(2, 1, -3, 3));
    final Binding<Background> selOff = Style.BACKGROUND.is(Background.blank()
            .inset(1, 2, -2, 2));

    private int tileCount = 9;
    private ScrollBar scroll;

    public LevelSelScreenEx(TrollsVsGoatsGame game) {
        super(game);
    }
    
    @Override
    protected Group createIface() {
    	root.addStyles(Style.BACKGROUND.is(Background
                .image(getImage("bg_level"))));
    	topPanel.addStyles(Style.BACKGROUND.is(Background.image(getImage("top_panel_bg"))));
    	
        topPanel.add(new Shim(0, 20));
        
        Group tiles = new Group(new AbsoluteLayout());
        Group myroot = new Group(new AbsoluteLayout());

        Icon dragerIcon = getIcon("scroll_b_active");
        iconChildBoard = getIcon("child_board");
        int y_pos = Y_START_POS ;
        
        for (int i = 1; i <= tileCount; i++) {
        	//add board
            tiles.add(AbsoluteLayout.at(new Label(iconChildBoard), 0, y_pos - 35, iconChildBoard.width(), iconChildBoard.height()));
            
            //add level number button
            Icon levelIcon = null;
            final int _i = i;
            
            if (i > model.maxCompletedLevel() + 1)
            {
            	levelIcon = getIcon("level_b_lock");
            	Button levelLockButton = new Button(levelIcon).setConstraint(Constraints.fixedSize(56, 56));
            	tiles.add(AbsoluteLayout.at(levelLockButton,40 , y_pos+57  - 35, 56,56));
            	levelLockButton.setStyles(Style.BACKGROUND.is(butBg), Style.ICON_POS.below);
            }
            else
            {
            	levelIcon = getIcon(Integer.toString(i));
            	final Button levelButton =  new Button(levelIcon).setConstraint(Constraints.fixedSize(56, 56));
            	tiles.add(AbsoluteLayout.at(levelButton,40 , y_pos+57  - 35, 56,56));
            	levelButton.setStyles(Style.BACKGROUND.is(butBg), Style.ICON_POS.below);
            	
            	
                levelButton.clicked().connect(new UnitSlot() {

                    @Override
                    public void onEmit() {
                        game.loadLevel(_i, false);
                    }
                });
                
                levelButton.layer.addListener(new Mouse.LayerAdapter() {
                    @Override
                    public void onMouseOver(MotionEvent event) {
                    	StringBuilder sb = new StringBuilder();
                    	sb.append(Integer.toString(_i));
                    	sb.append("_a");
                    	
                    	Icon levelIcon_a = getIcon(sb.toString());
                    	levelButton.icon.update(levelIcon_a);
                    }

                    @Override
                    public void onMouseOut(MotionEvent event) {
                    	levelButton.icon.update(getIcon(Integer.toString(_i)));
                    }
                });
                
                //add starts
                score = model.levelScore(i);
                if (score > 3)
                    score = 3;
                else if (score < 0)
                    score = 0;

                for (int j = 1; j <= 3; j++) {
                    if (j <= score)
                    {
                    	if(j == 1)
                    		tiles.add(AbsoluteLayout.at(new Label(getIcon("star")), 117, y_pos + 24 , 27, 26));
                    	else if(j == 2)
                    		tiles.add(AbsoluteLayout.at(new Label(getIcon("star")), 152, y_pos + 14 , 27, 26));
                    	else if(j == 3)
                    		tiles.add(AbsoluteLayout.at(new Label(getIcon("star")), 187, y_pos + 24 , 27, 26));
                    }
                }
            }

            y_pos += iconChildBoard.height() - 30; //30 - hanger rope height
        }
        
        myroot.add(AbsoluteLayout.at(tiles, 575, 13,iconChildBoard.width(), iconChildBoard.height() * tileCount));

        int scrollRange = tileCount * (int)iconChildBoard.height();
        float PAGE_SIZE = ((height() - SCROLL_HEIGHT) / (int)iconChildBoard.height()) + 1;
        float PAGE_RANGE = PAGE_SIZE * ((int)iconChildBoard.height());

        scroll = new ScrollBar(tiles, scrollRange, PAGE_RANGE );
        
        scroll.setBarBackgroundImage(getImage("scroll_bar"));
        scroll.setdraggerHeight(dragerIcon.height());
        scroll.setBarSize(50, height() - SCROLL_HEIGHT);
        
        final Button up = scroll.upButton();
        final Button down = scroll.downButton();
        final Button dragger = scroll.draggerButton();
        dragger.icon.update(dragerIcon);
        dragger.setStyles(Style.BACKGROUND.is(Background.blank()));
        
        up.addStyles(selOff).icon.update(getIcon("scroll_up_off"));
        down.addStyles(selOff).icon.update(getIcon("scroll_down_off"));
        addButtonListener(up);
        addButtonListener(down);
        if (scrollRange > PAGE_RANGE)
        {
        	myroot.add(AbsoluteLayout.at(scroll, 755, Y_START_POS + 15,iconChildBoard.width(), iconChildBoard.height()));
        }

        return myroot;
    }

    protected void addButtonListener(final Button button) {
        button.layer.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseDown(ButtonEvent event) {
                button.addStyles(selOn);
            }

            @Override
            public void onMouseUp(ButtonEvent event) {
                button.addStyles(selOff);
            }
        });
    }

    @Override
    public String[] images() {
        ArrayList<String> names = new ArrayList<String>();
        for (Badge badge : model.badges()) {
            names.add("badge_" + badge.name());
        }
        
        names.add("bg_level");
        names.add("scroll_up_off");
        names.add("scroll_down_off");
        names.add("badge_lock");
        names.add("scroll_b_active");
        names.add("scroll_bar");
        names.add("child_board");
        names.add("top_panel_bg");
        names.add("1");
        names.add("2");
        names.add("3");
        names.add("4");
        names.add("5");
        names.add("6");
        names.add("7");
        names.add("8");
        names.add("9");
        names.add("10");
        names.add("1_a");
        names.add("2_a");
        names.add("3_a");
        names.add("4_a");
        names.add("5_a");
        names.add("6_a");
        names.add("7_a");
        names.add("8_a");
        names.add("9_a");
        names.add("10_a");
        names.add("star");
        names.add("level_b_lock");

        return names.toArray(new String[names.size()]);
    }
    
    @Override
    protected String title() {
        return " ";
    }

    @Override
    public void wasShown() {
        wasAdded();
    }

    @Override
    public void wasHidden() {
        PlayN.keyboard().setListener(null);
    }

    @Override
    public void update(int delta) {
        if (scroll.isUpButtonDown())
            scroll.scrollUp(BUTTON_SCROLL_DISTANCE);
        else if (scroll.isDownButtonDown())
            scroll.scrollDown(BUTTON_SCROLL_DISTANCE);
        super.update(delta);
    }
    
}
