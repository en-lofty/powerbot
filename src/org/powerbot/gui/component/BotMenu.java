package org.powerbot.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.powerbot.game.bot.Bot;
import org.powerbot.gui.BotAbout;
import org.powerbot.gui.BotAccounts;
import org.powerbot.gui.BotChrome;
import org.powerbot.gui.BotSignin;
import org.powerbot.gui.BotWidgetExplorer;
import org.powerbot.service.NetworkAccount;
import org.powerbot.util.Configuration;
import org.powerbot.util.io.Resources;

/**
 * @author Paris
 */
public final class BotMenu extends JPopupMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	public final BotToolBar parent;

	public BotMenu(final BotToolBar parent) {
		this.parent = parent;

		final int tabs = parent.getTabCount();

		final JMenuItem newtab = new JMenuItem(BotLocale.NEWTAB);
		newtab.setIcon(new ImageIcon(Resources.getImage(Resources.Paths.TAB_ADD)));
		newtab.setEnabled(BotChrome.MAX_BOTS - tabs > 0);
		newtab.addActionListener(this);
		final JMenuItem closetab = new JMenuItem(BotLocale.CLOSETAB);
		closetab.setIcon(new ImageIcon(Resources.getImage(Resources.Paths.TAB_DELETE)));
		closetab.setEnabled(tabs > 0);
		closetab.addActionListener(this);
		add(newtab);
		add(closetab);
		addSeparator();

		final JMenuItem accounts = new JMenuItem(BotLocale.ACCOUNTS);
		accounts.setIcon(new ImageIcon(Resources.getImage(Resources.Paths.REPORT_KEY)));
		accounts.addActionListener(this);
		add(accounts);
		addSeparator();

		final JMenuItem signin = new JMenuItem(BotLocale.SIGNIN + "...");
		signin.setIcon(new ImageIcon(Resources.getImage(Resources.Paths.KEY)));
		if (NetworkAccount.getInstance().isLoggedIn()) {
			signin.setText(BotLocale.SIGNEDINAS + " " + NetworkAccount.getInstance().getAccount().getDisplayName());
		}
		add(signin);
		signin.addActionListener(this);
		addSeparator();

		final JMenu view;
		if (parent.getActiveTab() == -1) {
			view = new JMenu(BotLocale.VIEW);
			view.setEnabled(false);
		} else {
			view = new BotMenuView(this);
		}
		add(view);
		addSeparator();

		if (Configuration.DEVMODE) {
			final JMenuItem widgetExplorer = new JMenuItem(BotLocale.WIDGETEXPLORER);
			widgetExplorer.setEnabled(parent.getActiveTab() != -1);
			widgetExplorer.addActionListener(this);
			add(widgetExplorer);
			addSeparator();
		}

		final JMenuItem site = new JMenuItem(BotLocale.WEBSITE);
		site.setIcon(new ImageIcon(Resources.getImage(Resources.Paths.ICON_SMALL)));
		site.addActionListener(this);
		add(site);
		final JMenuItem about = new JMenuItem(BotLocale.ABOUT);
		about.setIcon(new ImageIcon(Resources.getImage(Resources.Paths.INFORMATION)));
		about.addActionListener(this);
		add(about);
		addSeparator();

		final JMenuItem exit = new JMenuItem(BotLocale.EXIT);
		exit.addActionListener(this);
		add(exit);
	}

	public void actionPerformed(final ActionEvent e) {
		final String a = e.getActionCommand();
		if (a.equals(BotLocale.NEWTAB)) {
			parent.addTab();
		} else if (a.equals(BotLocale.CLOSETAB)) {
			parent.closeTab(parent.getActiveTab());
		} else if (a.equals(BotLocale.ACCOUNTS)) {
			new BotAccounts(parent.parent);
		} else if (a.startsWith(BotLocale.SIGNIN) || a.startsWith(BotLocale.SIGNEDINAS)) {
			new BotSignin(parent.parent);
		} else if (a.equals(BotLocale.WIDGETEXPLORER)) {
			BotWidgetExplorer.display(Bot.bots.get(parent.getActiveTab()));
		} else if (a.equals(BotLocale.WEBSITE)) {
			BotChrome.openURL(Resources.getServerLinks().get("site"));
		} else if (a.equals(BotLocale.ABOUT)) {
			new BotAbout(parent.parent);
		} else if (a.equals(BotLocale.EXIT)) {
			parent.parent.dispatchEvent(new WindowEvent(parent.parent, WindowEvent.WINDOW_CLOSING));
		}
	}
}
