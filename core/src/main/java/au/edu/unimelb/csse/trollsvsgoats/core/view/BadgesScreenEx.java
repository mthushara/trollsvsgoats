package au.edu.unimelb.csse.trollsvsgoats.core.view;

import java.util.ArrayList;

import playn.core.Font;
import playn.core.Key;
import playn.core.Mouse;
import playn.core.PlayN;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse.ButtonEvent;
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

public class BadgesScreenEx extends View {

	private final static int TILE_WIDTH = 400;
    public final static int ICON_WIDTH = 60;
    public final static int DESCRIPTION_WIDTH = TILE_WIDTH - ICON_WIDTH;
    private final static int SCROLL_HEIGHT = 300;
    private final static int BUTTON_SCROLL_DISTANCE = 10;
    final Binding<Background> selOn = Style.BACKGROUND.is(Background.blank()
            .inset(2, 1, -3, 3));
    final Binding<Background> selOff = Style.BACKGROUND.is(Background.blank()
            .inset(1, 2, -2, 2));

    private int tileCount = 10;
    private ScrollBar scroll;
    
    Icon iconChildBoard = null;
    private final int Y_START_POS = -22;
    Styles bigLabel = Styles.make(
            Style.FONT.is(PlayN.graphics().createFont("Helvetica", Font.Style.BOLD, 15)),
            Style.HALIGN.center, 
            Style.COLOR.is(0xFFFFFFFF));

    
    public BadgesScreenEx(TrollsVsGoatsGame game) {
        super(game);
    }

    private void addCheatKeys() {
        PlayN.keyboard().setListener(new Listener() {

            @Override
            public void onKeyUp(Event event) {
                if (event.key().equals(Key.ENTER)) {
                    for (Badge badge : model.badges())
                        badge.setAchieved();
                    wasAdded();
                }
            }

            @Override
            public void onKeyTyped(TypedEvent event) {
            }

            @Override
            public void onKeyDown(Event event) {
            }
        });
    }

    @Override
    protected Group createIface() {
    	root.addStyles(Style.BACKGROUND.is(Background
                .image(getImage("badges_bg"))));
    	topPanel.addStyles(Style.BACKGROUND.is(Background.image(getImage("top_badge"))));
    	
        topPanel.add(new Shim(0, 15));
        
        Group tiles = new Group(new AbsoluteLayout());
        Group myroot = new Group(new AbsoluteLayout());

        Icon dragerIcon = getIcon("scroll_b_active");
 
        iconChildBoard = getIcon("badge_board");
        int y_pos = Y_START_POS ;
        
        Badge[] badges = model.badges();
        Icon badgeIcon = null;
        
        for (int i = 0; i < badges.length; i++) {
        	
        	Badge badge = badges[i];
        	//add board
            tiles.add(AbsoluteLayout.at(new Label(iconChildBoard), 0, y_pos - 35, iconChildBoard.width(), iconChildBoard.height()));
            
            
            final int _i = i;
            
            if (!badge.isAchieved())
            {
            	badgeIcon = getIcon("level_b_lock");
            	Label badgeLockButton = new Label(badgeIcon).setConstraint(Constraints.fixedSize(56, 56));
            	tiles.add(AbsoluteLayout.at(badgeLockButton,40 , y_pos+57  - 35, 56,56));
            }
            else
            {
            	badgeIcon = getIcon(badge.iconName());
            	final Label badgeButton =  new Label(badgeIcon).setConstraint(Constraints.fixedSize(56, 56));
            	tiles.add(AbsoluteLayout.at(badgeButton,40 , y_pos+57  - 35, 56,56));
            }
            
            final Label l = new Label(badge.description());
        	tiles.add(AbsoluteLayout.at((new Label(badge.displayName()).addStyles(bigLabel)), 117, y_pos + 20 , 150, 26));
        	tiles.add(AbsoluteLayout.at(l, 117, y_pos + 20 + 26, 150, 26));
        	
            y_pos += iconChildBoard.height() - 30; //30 - hanger rope height
        }
        
        for (int i = 0; i < tileCount - badges.length; i++) {
        	
        	//add board
            tiles.add(AbsoluteLayout.at(new Label(iconChildBoard), 0, y_pos - 35, iconChildBoard.width(), iconChildBoard.height()));
        	
        	badgeIcon = getIcon("level_b_lock");
        	Label badgeLockButton = new Label(badgeIcon).setConstraint(Constraints.fixedSize(56, 56));
        	tiles.add(AbsoluteLayout.at(badgeLockButton,40 , y_pos+57  - 35, 56,56));
        	
        	tiles.add(AbsoluteLayout.at((new Label("?").addStyles(bigLabel)), 117, y_pos + 20 , 150, 26));
        	
        	y_pos += iconChildBoard.height() - 30; //30 - hanger rope height
        }
        
        myroot.add(AbsoluteLayout.at(tiles, 575, 13,iconChildBoard.width(), iconChildBoard.height() * tileCount));

        int scrollRange = tileCount * (int)iconChildBoard.height();
        float PAGE_SIZE = ((height() - SCROLL_HEIGHT) / (int)iconChildBoard.height()) + 1;
        float PAGE_RANGE = PAGE_SIZE * ((int)iconChildBoard.height());

        //->scroll = new ScrollBar(tiles, scrollRange, 200 );
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
    protected Group createButtomPanel() {
    	return null;
    }

    @Override
    public String[] images() {
        ArrayList<String> names = new ArrayList<String>();
        for (Badge badge : model.badges()) {
            names.add("badge_" + badge.name());
        }
        names.add("scroll_up");
        names.add("scroll_down");
        names.add("badge_lock");
        names.add("top_badge");
        names.add("badges_bg");
        names.add("badge_board");
        
        return names.toArray(new String[names.size()]);
    }

    @Override
    protected String title() {
        return " ";
    }

    @Override
    public void wasShown() {
        addCheatKeys();
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
