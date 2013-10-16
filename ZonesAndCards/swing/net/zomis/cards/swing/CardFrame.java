package net.zomis.cards.swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.zomis.cards.events.PhaseChangeEvent;
import net.zomis.cards.events.ZoneChangeEvent;
import net.zomis.cards.hearts.HeartsGame;
import net.zomis.cards.hearts.HeartsGiveDirection;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.swing.MenuItemBuilder;
import net.zomis.custommap.view.swing.SimpleAction;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;
import net.zomis.events.Event;
import net.zomis.events.EventListener;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;


public class CardFrame extends JFrame implements EventListener, CardViewClickListener {
	private final CardGame game;

	private static final long	serialVersionUID	= 4987846986712417351L;
	private static final int	VIEW_LIMIT	= 10;
	private final JPanel	contentPane;
	private final JTextArea text = new JTextArea();
	private final List<CardZoneView> zoneViews = new LinkedList<CardZoneView>();
	private final JPanel panel;

	public CardFrame() {
		ZomisSwingLog4j.addConsoleAppender(null);
		new CustomFacade(new Log4jLog("Cards"));
		
		CustomFacade.getLog().i("Creating game");
//		this.game = new TurnEightGame().addPlayer("BUBU").addPlayer("Zomis").addPlayer("Minken");
//		this.game = new IdiotGame();
		this.game = new HeartsGame(HeartsGiveDirection.NONE).addPlayer("BUBU").addPlayer("Minken").addPlayer("Tejpbit").addPlayer("Zomis");
		this.game.setRandomSeed(42);
//		this.game = new MDJQGame();
//		this.game = new CWars2Game();
		CustomFacade.getLog().i("Game created");
		
		this.game.startGame();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 700);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
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
		
		menuA.add(new MenuItemBuilder("Not_Used", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		}).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menuA.add(new MenuItemBuilder("Perform Stack Action", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				StackAction action = getGame().processStackAction();
				CustomFacade.getLog().i("Processed Action: " + action);
				updateViews();
			}
		}).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menuA.add(new MenuItemBuilder(new SimpleAction("Next Phase", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().nextPhase();
			}
		})).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menu.add(menuA);
		

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		final JComboBox<CardViewTextStrategy> textMode = new JComboBox<CardViewTextStrategy>();
		textMode.setModel(new DefaultComboBoxModel<CardViewTextStrategy>(new CardViewTextStrategy[]{ new TextCardToString(), new TextCardModelName(), new TextActionString() }));
		textMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				CardView.text = (CardViewTextStrategy) textMode.getSelectedItem();
				updateViews();
			}
		});
		panel_1.add(textMode);
		
		final JCheckBox showEverything = new JCheckBox("Show All Cards");
		showEverything.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CardView.allKnown = showEverything.isSelected();
			}
		});
		panel_1.add(showEverything);
		
		final JSpinner viewLimit = new JSpinner();
		viewLimit.setValue(13);
		viewLimit.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int i = (Integer) viewLimit.getValue();
				for (CardZoneView zv : zoneViews)
					zv.setViewLimit(i);
			}
		});
		panel_1.add(viewLimit);
		
		this.setupGame();
		updateViews();
		
	}
	
	private static class TextCardToString implements CardViewTextStrategy {
		@Override
		public String textFor(Card card) {
			return card.toString();
		}
	}
	private static class TextCardModelName implements CardViewTextStrategy {
		@Override
		public String textFor(Card card) {
			return card.getModel().getName();
		}
	}
	private static class TextActionString implements CardViewTextStrategy {
		@Override
		public String textFor(Card card) {
			StackAction action = card.getGame().getAIHandler().click(card);
			return String.valueOf(action);
		}
	}
	
	private void setupGame() {
		getGame().registerListener(this);
		
		// Setup gameviews:
		for (CardZone zone : this.getGame().getPublicZones()) {
			CardZoneView zoneView = new CardZoneView(zone);
			zoneView.setViewLimit(VIEW_LIMIT);
			zoneViews.add(zoneView);
			zoneView.setListener(this);
			panel.add(zoneView.getThisPanel());
		}
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
		panel.add(text);
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
		updateViews();
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
