package net.zomis.cards.swing;

import java.awt.GridLayout;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceMap;

public class PlayerSummaryPanel extends JPanel {
	private static final long	serialVersionUID	= 5433351678830501566L;
	
	private final CardGame game;

	public PlayerSummaryPanel(CardGame game) {
		this.game = game;
		
		this.updateStatus();
	}

	public void updateStatus() {
		this.removeAll();
		
		final Set<IResource> resources = new TreeSet<IResource>(new ResourceMap.ResourceComparator());
		for (Player player : this.game.getPlayers()) {
			resources.addAll(player.getResources().getKeys());
		}
		
		this.setLayout(new GridLayout(3, resources.size(), 5, 3));
		
		this.addHeader(resources);
		for (Player player : this.game.getPlayers()) {
			JLabel name = new JLabel(player.getName());
			this.add(name);
			for (IResource res : resources) {
				JLabel resLabel = new JLabel();
				resLabel.setText(String.valueOf(player.getResources().getResources(res)));
				this.add(resLabel);
			}
		}
		
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
