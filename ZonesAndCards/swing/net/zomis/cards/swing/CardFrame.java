package net.zomis.cards.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.events.card.CardCreatedEvent;
import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.cards.events.game.AfterActionEvent;
import net.zomis.cards.events.game.GameOverEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.events.zone.ZoneReverseEvent;
import net.zomis.cards.events.zone.ZoneShuffleEvent;
import net.zomis.cards.events.zone.ZoneSortEvent;
import net.zomis.cards.hearts.HeartsSuperGame;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
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
	private static final int	VIEW_LIMIT	= 13;
	private final JPanel	contentPane;
	private final JTextArea text = new JTextArea();
	private final List<CardZoneView> zoneViews = new LinkedList<CardZoneView>();
	private final JPanel panelGameZones;
	private final JList<StackAction> actionList = new JList<StackAction>();
	
	private boolean	aiAutoplay;

	private final PlayerSummaryPanel playerSummary;

	public CardFrame() {
		ZomisSwingLog4j.addConsoleAppender(Log4jLog.DETAILED_LAYOUT);
		new CustomFacade(new Log4jLog("Cards"));
		
		CustomFacade.getLog().i("Creating game");
//		this.game = new TurnEightGame().addPlayer("BUBU").addPlayer("Zomis").addPlayer("Minken");
//		this.game = new IdiotGame();
		this.game = new HeartsSuperGame(new String[]{ "BUBU", "Minken", "Tejpbit", "Zomis"});
//		this.game = new HeartsGame(HeartsGiveDirection.NONE).addPlayer("BUBU").addPlayer("Minken").addPlayer("Tejpbit").addPlayer("Zomis");

//		this.game = new MDJQGame();
//		this.game = new CWars2Game();
		this.game.setRandomSeed(42);
		if (this.game instanceof CWars2Game) {
			((CWars2Game) this.game).addDefaultDecks();
		}
		
//		this.game.getPlayers().get(1).setAI(new CWars2AI_InstantWin());
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
				game.callPlayerAI();
			}
		}).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menuA.add(new JCheckBoxMenuItem(new SimpleAction("AI AutoPlay", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				aiAutoplay = ((JCheckBoxMenuItem) event.getSource()).isSelected();
			}
		})));
		
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
//				updateViews();
			}
		}).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menuA.add(new MenuItemBuilder(new SimpleAction("Next Phase", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getGame().addAndProcessStackAction(new NextTurnAction(getGame()));
			}
		})).setShortcut(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK)).getItem());
		menu.add(menuA);
		
		JPanel panelActions = new JPanel();
		contentPane.add(panelActions, BorderLayout.EAST);
		
		actionList.setModel(new DefaultListModel<StackAction>());
		actionList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					StackAction sel = actionList.getSelectedValue();
					if (sel != null)
						game.addAndProcessStackAction(sel);
				}
			}
		});
		panelActions.add(actionList);

		panelGameZones = new JPanel();
		contentPane.add(panelGameZones, BorderLayout.CENTER);
		panelGameZones.setLayout(new BoxLayout(panelGameZones, BoxLayout.Y_AXIS));
		
		JPanel panelSettings = new JPanel();
		contentPane.add(panelSettings, BorderLayout.SOUTH);
		
		final JComboBox<CardViewTextStrategy> textMode = new JComboBox<CardViewTextStrategy>();
		textMode.setModel(new DefaultComboBoxModel<CardViewTextStrategy>(new CardViewTextStrategy[]{ new TextCardToString(), new TextCardModelName(), new TextActionString() }));
		textMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				CardView.text = (CardViewTextStrategy) textMode.getSelectedItem();
				updateViews();
			}
		});
		panelSettings.add(textMode);
		
		final JCheckBox showEverything = new JCheckBox("Show All Cards");
		showEverything.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CardView.allKnown = showEverything.isSelected();
			}
		});
		panelSettings.add(showEverything);
		
		final JSpinner viewLimit = new JSpinner();
		viewLimit.setValue(VIEW_LIMIT);
		viewLimit.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int i = (Integer) viewLimit.getValue();
				for (CardZoneView zv : zoneViews)
					zv.setViewLimit(i);
			}
		});
		panelSettings.add(viewLimit);
		
		JPanel panelPlayers = new JPanel();
		contentPane.add(panelPlayers, BorderLayout.NORTH);
		playerSummary = new PlayerSummaryPanel(game);
		panelPlayers.add(playerSummary);
		
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
			StackAction action = card.getGame().getActionHandler().click(card);
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
			panelGameZones.add(zoneView.getThisPanel());
		}
	}
	
	@Event(priority=1000)
	public void evCardCreatedEvent(CardCreatedEvent event) {
		for (CardZoneView zv : zoneViews) {
			if (zv.getZone() == event.getZone())
				zv.addCard(event.getCard());
		}
	}
	@Event(priority=1000)
	public void evPhaseChangeEvent(PhaseChangeEvent event) {
		CustomFacade.getLog().i("Phase Change: " + event);
		updateViews();
	}
	@Event(priority=1000)
	public void evZoneShuffleEvent(ZoneShuffleEvent event) {
		recreateViewFor(event.getZone());
	}
	private void recreateViewFor(CardZone zone) {
		for (CardZoneView view : this.zoneViews) {
			if (view.getZone() == zone)
				view.recreateFromScratch();
		}
	}

	@Event(priority=1000)
	public void evZoneSortEvent(ZoneSortEvent event) {
		recreateViewFor(event.getZone());
	}
	@Event(priority=1000)
	public void evZoneShuffleEvent(ZoneReverseEvent event) {
		recreateViewFor(event.getZone());
	}

	private void updateViews() {
		DefaultListModel<StackAction> model = (DefaultListModel<StackAction>) this.actionList.getModel();
		model.clear();
		
		for (CardZoneView zv : zoneViews) {
			zv.updateTexts();
		}
		updateGameStatus();
		
		for (StackAction element : game.getActionHandler().getAvailableActions(game, game.getCurrentPlayer())) {
			if (element.actionIsAllowed())
				model.addElement(element);
		}
	}
	@Event(priority=1000)
	public void evAfterAction(AfterActionEvent event) {
		updateViews();
		this.playerSummary.updateStatus();
		if (game.getCurrentPlayer() != null && game.getCurrentPlayer().getAI() != null) {
			if (aiAutoplay && !game.isGameOver())
				game.callPlayerAI();
		}
	}
	
	@Event(priority=1000)
	public void evZoneChangeEvent(ZoneChangeEvent event) {
		CustomFacade.getLog().i("Zone Change: " + event);
		for (CardZoneView zv : zoneViews) {
			zv.onZoneChangeEvent(event);
		}
		updateGameStatus();
	}

	private void updateGameStatus() {
		panelGameZones.add(text);
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
		cardView.getCard().getGame().addAndProcessStackAction(game.getActionHandler().click(cardView.getCard()));
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

	@Event(priority = 1000)
	public void gameOver(GameOverEvent event) {
		if (!event.isCancelled())
			JOptionPane.showMessageDialog(this, "Game Finished!");
	}
	
}
