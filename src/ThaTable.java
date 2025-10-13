import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.CellEditor;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ThaTable extends JPanel{

    JTable table;
    String[] columnNames = {"Artist", "Title", "Length"};
    String rowData[][];
    BufferedImage icon;
    DefaultTableModel model;


    public JPanel tableInit(String[][] rData){

        this.rowData = rData;

        model = new DefaultTableModel(rData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        };


        table = new JTable(model);
        table.setOpaque(false);
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)table.getDefaultRenderer(Object.class);

        renderer.setOpaque(false);
        icon = LoadImg("/assets/backBG.jpg");
        JTableHeader header = table.getTableHeader();

        header.setReorderingAllowed(false);
        header.setFont(new Font("dogica pixel", Font.BOLD, 10));
        header.setBackground(new Color(151, 79, 104));
       // table.getTableHeader().setReorderingAllowed(false);
       // table.setBounds(0, 0, 700, 290);
       System.out.println("row selected: " + table.getSelectedRowCount());
        table.setSize(700, 290);
        table.setBackground(Color.black);
        table.setForeground(Color.black);
        table.setFont(new Font("dogica pixel", Font.PLAIN, 8));
        table.setGridColor(Color.black);
        table.setSelectionMode(1);
        table.setFillsViewportHeight(true);

        JScrollPane sp = new JScrollPane(table);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
       // sp.setBounds(0, 0, 500, 290);
        JPanel bg = new JPanel(new BorderLayout()){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);

                g.drawImage(icon, 0, 0, 485, 300, null);

            }
        };
        bg.add(sp);
        return bg;
    }

    //ignore this
    public void updateData(String[][] newData){
     // Clear existing data
        model.setRowCount(0);
        
        // Add new data
        for (String[] row : newData) {
            model.addRow(row);
        }
    }

    public BufferedImage LoadImg(String path){
        BufferedImage img = null;
        try {
            // Load image from classpath
            InputStream imgStream = getClass().getResourceAsStream(path);
            if (imgStream != null) {
                img = ImageIO.read(imgStream);
            } else {
                System.err.println("Image not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public JTable getTable() {
        return table;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public String[][] getRowData() {
        return rowData;
    }

    

}
