/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.workspace.actions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.simbrain.workspace.gui.SimbrainDesktop;

/**
 * Clear the current workspace.
 */
public final class ClearWorkspaceAction extends WorkspaceAction {

    private static final long serialVersionUID = 1L;

    private SimbrainDesktop desktop;

    /**
     * Create a clear workspace action with the specified workspace.
     * @param desktop
     */
    public ClearWorkspaceAction(SimbrainDesktop desktop) {
        super("Clear Workspace", desktop.getWorkspace());
        this.desktop = desktop;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_K,
            toolkit.getMenuShortcutKeyMask());
        putValue(ACCELERATOR_KEY, keyStroke);
    }

    /** @see AbstractAction 
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        desktop.clearDesktop();
    }
}