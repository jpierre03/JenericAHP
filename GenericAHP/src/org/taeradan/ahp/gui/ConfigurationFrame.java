/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericAHP.
 * 
 * GenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.taeradan.ahp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.taeradan.ahp.Criteria;
import org.taeradan.ahp.Indicator;
import org.taeradan.ahp.Root;


/**
 * Graphical user interface to configure the AHP tree and the preference matrix
 * @author  Yves Dubromelle
 */
public class ConfigurationFrame extends javax.swing.JFrame
{
	private DefaultTreeModel guiAhpTree;
//	private File currentFile = new File(System.getProperty("user.home"));
	private File currentFile = new File(this.getClass().getResource("/org/taeradan/ahp/conf").getFile());
	private Root ahpRoot;
	private boolean fileOpened = false;
	
	/** Creates new form ConfigurationFrame */
	public ConfigurationFrame()
	{	
//		Instanciation of an empty TreeModel
		guiAhpTree = new DefaultTreeModel(new DefaultMutableTreeNode());
//		Instantiation of an empty AHP root to use as default while no file is loaded
		ahpRoot = new Root();
//		The real AHP tree is attached to the graphical TreeModel to be displayed dynamically
		guiAhpTree.setRoot(processAhpHierarchy(ahpRoot));
		initComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents()
        {

                jFileChooser = new javax.swing.JFileChooser();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTreeAhp = new javax.swing.JTree();
                jMenuBar1 = new javax.swing.JMenuBar();
                jMenuFile = new javax.swing.JMenu();
                jMenuItemOpen = new javax.swing.JMenuItem();
                jMenuItemSave = new javax.swing.JMenuItem();
                jMenuItemSaveUnder = new javax.swing.JMenuItem();
                jMenuItemQuit = new javax.swing.JMenuItem();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("AHP Tree Configuration");

                jTreeAhp.setModel(guiAhpTree);
                jTreeAhp.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(java.awt.event.MouseEvent evt)
                        {
                                jTreeAhpMouseClicked(evt);
                        }
                });
                jScrollPane1.setViewportView(jTreeAhp);

                jMenuFile.setText("File");

                jMenuItemOpen.setText("Open");
                jMenuItemOpen.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                jMenuItemOpenActionPerformed(evt);
                        }
                });
                jMenuFile.add(jMenuItemOpen);

                jMenuItemSave.setText("Save");
                jMenuItemSave.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                jMenuItemSaveActionPerformed(evt);
                        }
                });
                jMenuFile.add(jMenuItemSave);

                jMenuItemSaveUnder.setText("Save under...");
                jMenuItemSaveUnder.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                jMenuItemSaveUnderActionPerformed(evt);
                        }
                });
                jMenuFile.add(jMenuItemSaveUnder);

                jMenuItemQuit.setText("Quit");
                jMenuFile.add(jMenuItemQuit);

                jMenuBar1.add(jMenuFile);

                setJMenuBar(jMenuBar1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

		/**
		 * Handles the actions of the mouse clicks.
		 * @param evt
		 */
		private void jTreeAhpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeAhpMouseClicked
//			System.out.println("bouton="+evt.getButton()+"nbClick"+evt.getClickCount());
//			If the mouse is over a valid tree node...
			if(jTreeAhp.getPathForLocation(evt.getX(), evt.getY()) != null){
//				Retrieve the selected node
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTreeAhp.getPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();
//				Handling of the the left button double click
				if(evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2){
					Object object = node.getUserObject();
					editActionPerformed(object);
				}
//				Handling of the right button double click
				if(evt.getButton() == MouseEvent.BUTTON3){
					final Object object = node.getUserObject();
					JPopupMenu contextMenu = new JPopupMenu();
					if(object instanceof Root){
						final Root root = (Root)object;
						JMenuItem addItem = new JMenuItem("Add criteria");
						addItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
							}
						});
//						addItem.addActionListener(this);
						contextMenu.add(addItem);
						JMenuItem editItem = new JMenuItem("Edit");
						editItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
								editActionPerformed(object);
							}
						});
						contextMenu.add(editItem);
						JMenuItem delItem = new JMenuItem("Delete");
						delItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
								delRootActionPerformed(root);
							}
						});
						contextMenu.add(delItem);
					}
					if(object instanceof Criteria){
						final Criteria criteria = (Criteria)object;
						JMenuItem addItem = new JMenuItem("Add indicator");
						addItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
							}
						});
						contextMenu.add(addItem);
						JMenuItem editItem = new JMenuItem("Edit");
						editItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
								editActionPerformed(object);
							}
						});
						contextMenu.add(editItem);
						JMenuItem delItem = new JMenuItem("Delete");
						delItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
								delCriteriaActionPerformed(criteria);
							}
						});
						contextMenu.add(delItem);
					}
					if(object instanceof Indicator){
						Indicator indicator = (Indicator)object;
						JMenuItem editItem = new JMenuItem("Edit");
						editItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
							}
						});
						contextMenu.add(editItem);
						JMenuItem delItem = new JMenuItem("Delete");
						delItem.addActionListener(new java.awt.event.ActionListener(){
							public void actionPerformed(java.awt.event.ActionEvent evt){
								editActionPerformed(object);
							}
						});
						contextMenu.add(delItem);
					}
					contextMenu.show(jTreeAhp, evt.getX(),evt.getY());
				}
			}
		}//GEN-LAST:event_jTreeAhpMouseClicked

		/**
		 * Handles the event of the "Open" menu.
		 * Launch a file selector to choose a configuration file to open.
		 * @param evt
		 */
		private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemOpenActionPerformed
		{//GEN-HEADEREND:event_jMenuItemOpenActionPerformed
			jFileChooser = new JFileChooser(currentFile);
			jFileChooser.setApproveButtonText("Open");
			jFileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
			if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				currentFile = jFileChooser.getSelectedFile();
				ahpRoot = new Root(new File(currentFile.getAbsolutePath()));
				guiAhpTree.setRoot(processAhpHierarchy(ahpRoot));
				fileOpened = true;
			}
		}//GEN-LAST:event_jMenuItemOpenActionPerformed

		/**
		 * Handles the event of the "Save" menu.
		 * It just save to the file previously opened.
		 * If no file is opened, lauch the "Save under" action.
		 * @param evt
		 */
		private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemSaveActionPerformed
		{//GEN-HEADEREND:event_jMenuItemSaveActionPerformed
			if (fileOpened)
			{	ahpRoot.saveConfig(currentFile.getAbsolutePath());
			}
			else
				jMenuItemSaveUnderActionPerformed(evt);
		}//GEN-LAST:event_jMenuItemSaveActionPerformed

		/**
		 * Handles the event of the "Save under" menu.
		 * Launch a file selector to choose a file to write.
		 * @param evt
		 */
		private void jMenuItemSaveUnderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemSaveUnderActionPerformed
		{//GEN-HEADEREND:event_jMenuItemSaveUnderActionPerformed
			jFileChooser = new JFileChooser(currentFile);
			jFileChooser.setApproveButtonText("Save");
			jFileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
			if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				currentFile = jFileChooser.getSelectedFile();
				ahpRoot.saveConfig(currentFile.getAbsolutePath());
			}
		}//GEN-LAST:event_jMenuItemSaveUnderActionPerformed
	
	private void editActionPerformed(Object object){
		if(object instanceof Root){
			Root root = (Root)object;
			RootDialog dialog = new RootDialog(this, true, root);
			dialog.setVisible(true);
		}
		if(object instanceof Criteria){
			Criteria criteria = (Criteria)object;
			CriteriaDialog dialog = new CriteriaDialog(this, true, criteria);
			dialog.setVisible(true);
		}
		if(object instanceof Indicator){
			Indicator indicator = (Indicator)object;
			IndicatorDialog dialog = new IndicatorDialog(this, true, indicator);
			dialog.setVisible(true);
		}
	}

	private void delRootActionPerformed(Root root) {
		if(JOptionPane.showConfirmDialog(this, "Are you sure ? The whole tree will be destroyed.", "Confirmation needed", JOptionPane.YES_NO_OPTION)==0){
			ahpRoot = new Root();
			guiAhpTree.setRoot(processAhpHierarchy(ahpRoot));
			editActionPerformed(ahpRoot);
		}
	}
	
	private void delCriteriaActionPerformed(Criteria criteria){
		if(JOptionPane.showConfirmDialog(this, "Are you sure ? The criteria and its indicators will be destroyed.", "Confirmation needed", JOptionPane.YES_NO_OPTION)==0){
			ahpRoot.delCriteria(criteria);
			guiAhpTree.setRoot(processAhpHierarchy(ahpRoot));
		}
	}
	
	private void delIndicatorActionPerformed(Indicator indicator){
		if(JOptionPane.showConfirmDialog(this, "Are you sure ? The indicator will be destroyed.", "Confirmation needed", JOptionPane.YES_NO_OPTION)==0){
			
		}
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[])
	{
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				new ConfigurationFrame().setVisible(true);
			}
		});
	}
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JFileChooser jFileChooser;
        private javax.swing.JMenuBar jMenuBar1;
        private javax.swing.JMenu jMenuFile;
        private javax.swing.JMenuItem jMenuItemOpen;
        private javax.swing.JMenuItem jMenuItemQuit;
        private javax.swing.JMenuItem jMenuItemSave;
        private javax.swing.JMenuItem jMenuItemSaveUnder;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTree jTreeAhp;
        // End of variables declaration//GEN-END:variables
	
	/**
	 * Takes an initialized AHP root element and produces a tree by processing the AHP hierarchy
	 * @param ahpRoot Initialised AHP root
	 * @return node containing a AHP tree
	 */
	public static DefaultMutableTreeNode  processAhpHierarchy(Root ahpRoot){
//		Creation of the root node
		DefaultMutableTreeNode guiRoot = new DefaultMutableTreeNode(ahpRoot);
		ArrayList<Criteria> ahpCriterias = ahpRoot.getCriterias();
		ArrayList<DefaultMutableTreeNode> guiCriterias = new ArrayList<DefaultMutableTreeNode>();
//		For each criteria in root
		for(int i=0; i<ahpCriterias.size(); i++){
//			Real criteria attached to a criteria node
			guiCriterias.add(new DefaultMutableTreeNode(ahpCriterias.get(i)));
//			Criteria node attached to the root node
			guiRoot.add(guiCriterias.get(i));
			ArrayList<Indicator> ahpIndicators = ahpCriterias.get(i).getIndicators();
			ArrayList<DefaultMutableTreeNode> guiIndicators = new ArrayList<DefaultMutableTreeNode>();
//			For each indicator in the criteria
			for(int j=0; j<ahpIndicators.size(); j++){
//				Real indicator attached to an indicator node
				guiIndicators.add(new DefaultMutableTreeNode(ahpIndicators.get(j)));
//				Indicator node attached to the criteria node
				guiCriterias.get(i).add(guiIndicators.get(j));
			}
		}
		return guiRoot;
	}
//	JOptionPane.showMessageDialog(this, evt);
}
