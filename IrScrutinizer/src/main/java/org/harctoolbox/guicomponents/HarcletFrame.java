/*
Copyright (C) 2013 Bengt Martensson.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or (at
your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program. If not, see http://www.gnu.org/licenses/.
*/

package org.harctoolbox.guicomponents;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class HarcletFrame extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    boolean exitOnClose;

    /**
     * Creates new form
     * @param panel
     * @param exitOnClose
     * @param lafClassName
     */
    public HarcletFrame(HarcPanel panel, boolean exitOnClose, String lafClassName) {
        try {
            if (lafClassName != null)
                UIManager.setLookAndFeel(lafClassName);
        } catch (ClassNotFoundException ex) {
            //error(ex);
        } catch (InstantiationException ex) {
            //error(ex);
        } catch (IllegalAccessException ex) {
            //error(ex);
        } catch (UnsupportedLookAndFeelException ex) {
            //error(ex);
        }
        this.exitOnClose = exitOnClose;
        harclet = panel;
        initComponents();
        setTitle(harclet.getProgName());

        setIconImage((new ImageIcon(HarcletFrame.class.getResource(harclet.getIconPath()))).getImage());
        setResizable(false);

        if (exitOnClose) {
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        }
    }

    public static HarcletFrame newHarcletFrame(JFrame parent, HarcPanel panel, boolean exitOnClose, String lafClassName) {
        HarcletFrame harcletFrame = new HarcletFrame(panel, exitOnClose, lafClassName);
        harcletFrame.setLocationRelativeTo(parent);
        harcletFrame.setVisible(true);
        return harcletFrame;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        //harclet = new org.harctoolbox.guicomponents.HexCalculator();
        jMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        closeMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        helpMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        fileMenu.setText("File");

        closeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        closeMenuItem.setText(exitOnClose ? "Exit" : "Close");
        closeMenuItem.setIcon(new javax.swing.ImageIcon(HarcletFrame.class.getResource("/icons/Crystal-Clear/22x22/actions/stop.png"))); // NOI18N
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                closeMenuItemActionPerformed(null);
            }
        });
        fileMenu.add(closeMenuItem);

        jMenuBar.add(fileMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About...");
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(HarcletFrame.class.getResource("/icons/Crystal-Clear/22x22/actions/info.png"))); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        helpMenuItem.setMnemonic('H');
        helpMenuItem.setText("Help...");
        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setIcon(new javax.swing.ImageIcon(HarcletFrame.class.getResource("/icons/Crystal-Clear/22x22/actions/help.png"))); // NOI18N
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        jMenuBar.add(helpMenu);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(harclet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(harclet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
         JOptionPane.showMessageDialog(this, harclet.getAboutMessage(), "About " + harclet.getProgName(),
                JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(HarcletFrame.class.getResource("/icons/Crystal-Clear/48x48/actions/info.png")));
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        JOptionPane.showMessageDialog(this, harclet.getHelpMessage(), harclet.getProgName() + " help",
                JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(HarcletFrame.class.getResource("/icons/Crystal-Clear/48x48/actions/info.png")));
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
         if (exitOnClose)
             doExit(0);
         else {
             harclet.close();
             dispose();
         }
    }//GEN-LAST:event_closeMenuItemActionPerformed

    private static void doExit(int exitcode) {
        System.exit(exitcode);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String lafName = null;
        String className = "TimeFrequencyCalculator";
        HarcPanel panel = null;

        if (args.length == 1)
            className = args[0];
        else if (args.length == 2) {
            lafName = args[0];
            className = args[1];
        }


        /* Create and display the form */
        final String laf = lafName;
        //String packageName = Package.getPackage(laf)
        try {
            Class<?> clazz = Class.forName("org.harctoolbox.guicomponents." + className);

            Constructor<?> constructor = clazz.getConstructor((Class<?>[]) null);

            panel = (HarcPanel) constructor.newInstance((Object[]) null);
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        } catch (InvocationTargetException ex) {
            System.err.println(ex.getMessage());
        } catch (NoSuchMethodException ex) {
            System.err.println(ex.getMessage());
        } catch (SecurityException ex) {
            System.err.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        final HarcPanel harcPanel = panel;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new HarcletFrame(new HexCalculator(), true, laf).setVisible(true);
                new HarcletFrame(harcPanel, true, laf).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private final HarcPanel harclet;
    private javax.swing.JMenuBar jMenuBar;
    // End of variables declaration//GEN-END:variables
}
