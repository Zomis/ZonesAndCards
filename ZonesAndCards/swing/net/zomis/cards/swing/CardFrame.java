package net.zomis.cards.swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.events.PhaseChangeEvent;
import net.zomis.cards.events.ZoneChangeEvent;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.swing.MenuItemBuilder;
import net.zomis.custommap.view.swing.SimpleAction;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;
import net.zomis.events.Event;
import net.zomis.events.EventListener;


public class CardFrame extends JFrame implements EventListener, CardViewClickListener {
	private final CardGame game;

	private static final long	serialVersionUID	= 4987846986712417351L;
	private static final int	VIEW_LIMIT	= 10;
	private final JPanel	contentPane;
	private final JTextArea text = new JTextArea();
	private final List<CardZoneView> zoneViews = new LinkedList<>();

	public CardFrame() {
		ZomisSwingLog4j.addConsoleAppender(null);
		new CustomFacade(new Log4jLog("Cards"));
		
//		this.game = new TurnEightGame(new String[]{ "Zomis", "BUBU", "Minken" });
//		this.game = new IdiotGame();
		this.game = new CWars2Game();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		
		JMenu menuA = new JMenu("Main");
		menuA.add(new MenuItemBuilder("Let AI Play", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AIHandler ai = game.getAIHandler();
				ai.move(game);
			}
		}).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menuA.add(new MenuItemBuilder("Next Phase", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getGame().log();
			}
		}).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menuA.add(new MenuItemBuilder(new SimpleAction("Next Phase", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().nextPhase();
			}
		})).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menu.add(menuA);
		
		this.setupGame();
		
	}
	private void setupGame() {
		getGame().registerListener(this);
		
		// Setup gameviews:
		for (CardZone zone : this.getGame().getZones()) {
			CardZoneView zoneView = new CardZoneView(zone);
			zoneView.setViewLimit(VIEW_LIMIT);
			zoneViews.add(zoneView);
			zoneView.setListener(this);
			contentPane.add(zoneView.getThisPanel());
		}
		
		contentPane.add(text);
		updateViews();
	}
	
	@Event
	public void evPhaseChangeEvent(PhaseChangeEvent event) {
		CustomFacade.getLog().i("Phase Change: " + event);
		updateViews();
	}

	private void updateViews() {
		for (CardZoneView zv : zoneViews) {
			zv.updateTexts();
		}
		updateGameStatus();
	}
	@Event
	public void evAfterAction(AfterActionEvent event) {
		updateViews();
	}
	
	@Event
	public void evZoneChangeEvent(ZoneChangeEvent event) {
		CustomFacade.getLog().i("Zone Change: " + event);
		for (CardZoneView zv : zoneViews) {
			zv.onZoneChangeEvent(event);
		}
		updateGameStatus();
	}

	private void updateGameStatus() {
		text.setText(getGame().toString());
		for (Player ee : getGame().getPlayers()) {
			text.append("\n");
			text.append(ee.toString());
		}
		text.append("\n");
		
		text.append("Active Phase: " + getGame().getActivePhase() + "\n");
	}

	public CardGame getGame() {
		return game;
	}

	@Override
	public void onCardClick(CardView cardView) {
		cardView.getCard().getGame().addAndProcessStackAction(game.getAIHandler().click(cardView.getCard()));
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CardFrame frame = new CardFrame();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
