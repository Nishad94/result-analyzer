import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class SearchScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private GUI homescreen;
	private Queries_GUI q_gui = new Queries_GUI();
	private JScrollPane scrollPane;
	private JPanel topPanel;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public SearchScreen(GUI g, final DB_Init db_table) {
		homescreen = g;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 745, 485);
		contentPane = new JPanel();
		contentPane.setForeground(Color.DARK_GRAY);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		ImageIcon homeIcon = new ImageIcon("home-icon.png");
		JButton homeBtn = new JButton(homeIcon);
		homeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				homescreen.setVisible(true);
				dispose();
			}
		});
		homeBtn.setBounds(10, 11, 72, 55);
		contentPane.add(homeBtn);
		
		textField = new JTextField();
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		textField.setBounds(420, 22, 170, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		String searchMenu[] = {"Name", "Roll No."};
		final JComboBox comboBox = new JComboBox(searchMenu);
		//comboBox.setModel(new DefaultComboBoxModel());
		comboBox.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		comboBox.setBounds(600, 22, 81, 28);
		contentPane.add(comboBox);
		
		List<String[]> data = q_gui.filterByCriteria("College", "Branch", "Batch", "Year", db_table);
		
		topPanel = new JPanel();
		topPanel.setForeground(Color.GRAY);
		topPanel.setBackground(Color.GRAY);
		topPanel.setBounds(0, 114, 729, 332);
		topPanel.setLayout( new BorderLayout());
		
		
		final tableModel table_model = new tableModel(data) ;
		final JTable table = new JTable(table_model);
		table.setRowHeight(35);
		table.setFont(new Font("Arial", Font.PLAIN, 13));
		table.setBounds(10, 127, 709, 308);
		table.setRowSelectionAllowed(true);
		table.setBackground(new Color(51, 51, 51));
		table.setForeground(Color.white);
		table.setSelectionForeground(Color.white);
		table.setSelectionBackground(Color.gray);
		table.getColumnModel().getColumn(0).setMaxWidth(75);
		//table.setForeground();
		scrollPane = table.createScrollPaneForTable(table);
		topPanel.add(scrollPane, BorderLayout.CENTER);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		contentPane.add(topPanel);
	
		table.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
			    if (e.getClickCount() == 1) {
			    	int row_sel = table.getSelectedRow();
					String name = table_model.getName(row_sel);
					//System.out.println(name);
					StudentScreen new_student_window = new StudentScreen(name, db_table);
					new_student_window.setVisible(true);
					
			      // do some action if appropriate column
			    }
			  }
			});
		
		final String collegeMenu[] = q_gui.collegeList(db_table);
		
		final JComboBox comboBox_1 = new JComboBox(collegeMenu);
		
		comboBox_1.setName("");
		comboBox_1.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		comboBox_1.setBounds(10, 77, 150, 28);
		contentPane.add(comboBox_1);
		
		final String branchMenu[] = q_gui.branchList(db_table);
		final JComboBox comboBox_2 = new JComboBox(branchMenu);
		comboBox_2.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		comboBox_2.setBounds(168, 77, 150, 28);
		contentPane.add(comboBox_2);
		
		final String batchMenu[] = q_gui.batchList(db_table);
		final JComboBox comboBox_3 = new JComboBox(batchMenu);
		comboBox_3.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		comboBox_3.setBounds(330, 77, 150, 28);
		contentPane.add(comboBox_3);
		
		final String yearMenu[] = {"Year", "FE", "SE", "TE", "BE"};
		final JComboBox comboBox_4 = new JComboBox(yearMenu);
		comboBox_4.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		comboBox_4.setBounds(493, 77, 150, 28);
		contentPane.add(comboBox_4);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 114, 739, 2);
		contentPane.add(separator);
		
		ImageIcon searchIcon = new ImageIcon("Search-icon.png");
		JButton searchBtn = new JButton(searchIcon);
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//setVisible(false);
				//homescreen.setVisible(true);
				//dispose();
				
				if(comboBox.getSelectedItem().toString() == "Name")
				{
					table_model.deleteAllRows();
					table_model.addTheseRows(q_gui.searchByName(textField.getText(), db_table));
				}
				else if(comboBox.getSelectedItem().toString() == "Roll No."){
					table_model.deleteAllRows();
					table_model.addTheseRows(q_gui.searchByRoll(textField.getText(), db_table));
				}
			}
		});
		
		
		searchBtn.setBounds(691, 22, 28, 28);
		contentPane.add(searchBtn);
		
		JButton btnNewButton = new JButton("Go");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hi");
				table_model.deleteAllRows();
				table_model.addTheseRows(q_gui.filterByCriteria(comboBox_1.getSelectedItem().toString(), comboBox_2.getSelectedItem().toString(),comboBox_3.getSelectedItem().toString(), comboBox_4.getSelectedItem().toString(), db_table));
			}
		});
		btnNewButton.setBounds(653, 78, 66, 25);
		contentPane.add(btnNewButton);
	}
}