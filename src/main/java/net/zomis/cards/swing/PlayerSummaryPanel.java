package net.zomis.cards.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;

public class PlayerSummaryPanel extends JPanel {
	private static final long	serialVersionUID	= 5433351678830501566L;
	
	private final CardGame<?, ?> game;

	private final ResourceMap[]	previous;

	public PlayerSummaryPanel(CardGame<?, ?> game) {
		this.game = game;
		this.previous = new ResourceMap[game.getPlayers().size()];
		this.updateStatus();
	}

	public void updateStatus() {
		this.removeAll();
		
		final Set<IResource> resources = new TreeSet<IResource>(new ResourceMap.ResourceComparator());
		for (Player player : this.game.getPlayers()) {
			resources.addAll(player.getResources().getKeys());
		}
//		if (resources.isEmpty()) resources.add(new ResourceType("NULL"));
		this.setLayout(new GridLayout(0, resources.size() + 1, 5, 3));
		
		this.addHeader(resources);
		int i = 0;
		for (Player player : this.game.getPlayers()) {
			update(player, i, resources);
		}
		
	}

	private void update(Player player, int i, Set<IResource> resources) {
		ResourceMap old = this.previous[i];
		if (old == null) old = new ResourceMap();
		JLabel name = new JLabel(player.getName());
		this.add(name);
		for (IResource res : resources) {
			JLabel resLabel = new JLabel();
			int curr = player.getResources().getResources(res);
			int change = curr - old.getResources(res);
			ResourceData data = player.getResources().dataFor(res);
			String extra = (data.getListener() == null ? "" : "!") + (data.getStrategy() == null ? "" : "*");
			if (change != 0)
				resLabel.setText(curr + " (" + change + ")" + extra);
			else resLabel.setText(curr + extra);
			
			if (change > 0) resLabel.setForeground(new Color(0, 128, 0));
			else if (change < 0) resLabel.setForeground(Color.RED);
			
			this.add(resLabel);
		}
		this.previous[i++] = new ResourceMap(player.getResources());
	}

	private void addHeader(Set<IResource> resources) {
		JLabel label = new JLabel("Player");
		this.add(label);
		for (IResource res : resources) {
			JLabel resLabel = new JLabel();
			resLabel.setText(res.toString());
			this.add(resLabel);
		}
		
	}
	

}
