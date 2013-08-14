package au.edu.unimelb.csse.trollsvsgoats.core.view;

import java.awt.Cursor;

import playn.core.Font;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.PlayN;
import au.edu.unimelb.csse.trollsvsgoats.core.TrollsVsGoatsGame;
import au.edu.unimelb.csse.trollsvsgoats.core.view.MessageBox.SimpleCallBack;
import react.UnitSlot;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;

public class MainScreenEx extends View {
	
	//these values are for the 1024x720 resolution.
	//you have to calculate these for the 800x600 resolution
	private final float LOCAL_TOP_MARGIN = 70;
	private final float X_POS = 610;
	private final float Y_POS_LABEL = 8;
	private final float Y_POS_BUTTON = 72;
	private final float Y_BUTTON_OFFSET = 80;
	private final float BUTTON_WIDTH = 275;
	private final float BUTTON_HEIGHT = 60;
	private final float LABEL_HEIGHT = 30;
	
	public MainScreenEx(TrollsVsGoatsGame game) {
        super(game);
        TOP_MARGIN = (int)LOCAL_TOP_MARGIN;
    }

    @Override
    protected Group createIface() {
    	root.addStyles(Style.BACKGROUND.is(Background
                .image(getImage("main_screen"))));
    	topPanel.addStyles(Style.BACKGROUND.is(Background.blank()));
    	
        back.setVisible(false);
        
        //calculating values for screen resolution
        int screen_width = model.screenWidth();
        int screen_height = model.screenHeight();
        
        float local_top_margin = (LOCAL_TOP_MARGIN/720)*screen_height;
        float top_margin_gap = LOCAL_TOP_MARGIN - local_top_margin;
        float x_pos = (X_POS/1024)*screen_width;
        float y_pos_label = (Y_POS_LABEL/720)*screen_height;
        float y_pos_button = (Y_POS_BUTTON/720)*screen_height;
        float y_button_offset = (Y_BUTTON_OFFSET/720)*screen_height;
        float button_width = (BUTTON_WIDTH/1024)*screen_width;
        float label_height = (LABEL_HEIGHT/720)*screen_height;
        float button_height = (BUTTON_HEIGHT/720)*screen_height;
        //
        
        Group buttons;
        buttons = new Group(new AbsoluteLayout());
        
        Styles bigLabel = Styles.make(
                Style.FONT.is(PlayN.graphics().createFont("Helvetica", Font.Style.BOLD, 20)),
                Style.HALIGN.center, 
                Style.COLOR.is(0xFFFFFFFF));
        final Label labelUser = new Label(game.userName()).addStyles(bigLabel);
        buttons.add(AbsoluteLayout.at(labelUser,x_pos ,y_pos_label - top_margin_gap, button_width, label_height ));
                
        
        final Button buttonStart = new Button();//START
        buttons.add(AbsoluteLayout.at(buttonStart,x_pos ,y_pos_button - top_margin_gap, button_width, button_height ).setStyles(Style.BACKGROUND.is(Background.blank())));
        buttonStart.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showLevelSelScreen(0);
            }
        });
        
         
        final Button buttonBadges = new Button();//BADGES
        y_pos_button += y_button_offset;
        buttons.add(AbsoluteLayout.at(buttonBadges,x_pos ,y_pos_button - top_margin_gap, button_width, button_height ).setStyles(Style.BACKGROUND.is(Background.blank())));
        buttonBadges.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showBadgesScreen();
            }
        });

        
        final Button buttonOptions = new Button(); //OPTIONS
        y_pos_button += y_button_offset;
        buttons.add(AbsoluteLayout.at(buttonOptions,x_pos ,y_pos_button - top_margin_gap, button_width, button_height).setStyles(Style.BACKGROUND.is(Background.blank())));
        buttonOptions.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showOptionScreen();
            }
        });

        final Button buttonLeaderBoard = new Button(); //todo - LEADER BOARD
        y_pos_button += y_button_offset;
        buttons.add(AbsoluteLayout.at(buttonLeaderBoard,x_pos ,y_pos_button - top_margin_gap, button_width, button_height).setStyles(Style.BACKGROUND.is(Background.blank())));
        buttonLeaderBoard.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
            	showTempMessageBox();
            }
        });
        
        final Button buttonHelp = new Button(); //HELP
        y_pos_button += y_button_offset;
        buttons.add(AbsoluteLayout.at(buttonHelp,x_pos ,y_pos_button - top_margin_gap, button_width, button_height).setStyles(Style.BACKGROUND.is(Background.blank())));
        buttonHelp.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                game.showHelpScreen();
            }
        });

        return buttons;
    }
    
    private void showTempMessageBox() {
		MessageBox temp = new MessageBox(game, "Under Construction", "OK",
                 new SimpleCallBack() {

                     @Override
                     public void onClose() {
                         game.closeMessageBox();
                     }
                 });
    	 
   	 	game.showMessageBox(this,temp);
	}

    @Override
    public String[] images() {
        return new String[] { "back_on", "back_off","main_screen" };
    }

    @Override
    protected String title() {

    	return null;
    }
}
