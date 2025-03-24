import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class Yahtzee implements ActionListener {
  JFrame frame;
  JTable table;
  private DefaultTableModel model;
  JPanel contentPane, contentPane2,contentPane3;
  JButton dice1, dice2, dice3, dice4, dice5, reroll, replay;
  JLabel label;
  int[] diceNums ={0,0,0,0,0};
  int playerTurn = 1;
  int roll=1;
  int totalRounds = 0;
  int[] playerRounds={0,0};
  int upTotal1=0;
  int downTotal1=0;
  int upTotal2=0;
  int downTotal2=0;
  int total1=0;
  int total2=0;
  int bonus1=0;
  int bonus2=0;
  int filled = 0;

  public Yahtzee() {
    frame = new JFrame("Yahtzee");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    contentPane = new JPanel();
    contentPane.setLayout(new GridLayout(1, 2, 10, 5));
    contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    contentPane2 = new JPanel();
    contentPane2.setLayout(new GridLayout(6, 1, 10, 5));

    contentPane3 = new JPanel();
    contentPane3.setLayout(new GridLayout(0, 1, 10, 5));

    
    contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    dice1=new JButton(new ImageIcon());
    dice1=decideButton(1,diceNums,dice1);
    dice1.addActionListener(this);
    dice2=new JButton(new ImageIcon());
    dice2=decideButton(2,diceNums,dice2);
    dice2.addActionListener(this);
    dice3=new JButton(new ImageIcon());
    dice3=decideButton(3,diceNums,dice3);
    dice3.addActionListener(this);
    dice4=new JButton(new ImageIcon());
    dice4=decideButton(4,diceNums,dice4);
    dice4.addActionListener(this);
    dice5=new JButton(new ImageIcon());
    dice5=decideButton(5,diceNums,dice5);
    dice5.addActionListener(this);
    
    reroll = new JButton("Reroll");
    reroll.setActionCommand("Reroll");
    reroll.addActionListener(this);

    replay = new JButton("Replay");
    replay.setActionCommand("Replay");
    replay.addActionListener(this);
    
    contentPane2.add(dice1);
    contentPane2.add(dice2);
    contentPane2.add(dice3);
    contentPane2.add(dice4);
    contentPane2.add(dice5);
    contentPane2.add(reroll);

    contentPane.add(contentPane2);
    

    String[] columnNames = {"", "Player 1", "Player2"};
    String[][] data = {{"Ones", "", ""}, {"Twos", "", ""}, {"Threes", "",""},{"Fours", "", ""}, {"Fives", "", ""}, {"Sixes", "", ""}, {"Three of a Kind", "", ""}, {"Four of a Kind", "", ""}, {"Full House", "", ""}, {"Small Straight", "", ""}, {"Large Straight", "", ""}, {"Chance", "", ""}, {"Yahtzee", "", ""}};

    model = new DefaultTableModel(data, columnNames);
    table = new JTable(model);
    table.setPreferredScrollableViewportSize(new Dimension(500, 300));
    table.setFillsViewportHeight(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setDefaultEditor(Object.class, null);
    table.getColumnModel().getColumn(0).setPreferredWidth(100);
    JScrollPane scrollPane = new JScrollPane(table);
    contentPane3.add(scrollPane);
    label = new JLabel("Player 1's turn");
    contentPane3.add(label);
    contentPane.add(contentPane3);
    table.addMouseListener(new java.awt.event.MouseAdapter(){
      @Override
      public void mouseClicked(java.awt.event.MouseEvent event){
      int row = table.rowAtPoint(event.getPoint());
      int col = table.columnAtPoint(event.getPoint());
        int ones=0;
        int twos=0;
        int threes=0;
        int fours=0;
        int fives=0;
        int sixes=0;

        for(int i=0;i<5;i++){
          if(diceNums[i]==1){
            ones++;
          }
          if(diceNums[i]==2){
            twos++;
          }
          if(diceNums[i]==3){
            threes++;
          }
          if(diceNums[i]==4){
            fours++;
          }
          if(diceNums[i]==5){
            fives++;
          }
          if(diceNums[i]==6){
            sixes++;
          }
        }
        boolean hasRolled = false;
        
        if(col>0 && col<3 && row>=0 && row<table.getRowCount() && col==playerTurn && table.getValueAt(row,col)==""){
          if (reroll.getActionCommand().equals("Reroll") || reroll.getActionCommand().equals("Done")) {
            hasRolled = true;
          }
          
          if(!reroll.getActionCommand().equals("Done")){
            reroll.setActionCommand("Done");
          }
          
          if(col==1 && table.getValueAt(row,col)=="" && reroll.getActionCommand().equals("Done") && hasRolled){
            playerRounds[playerTurn-1]++;
            totalRounds++;
            System.out.println(playerRounds[0]+" " + playerRounds[1] + " " + totalRounds);
            calculateScore(row,col,ones,twos,threes,fours,fives,sixes);
            playerTurn=2;
            label.setText("Player 2's turn");
          }else if (col==2 && table.getValueAt(row,col)=="" && reroll.getActionCommand().equals("Done") && hasRolled){
            playerRounds[playerTurn-1]++;
            totalRounds++;
            System.out.println(playerRounds[0]+" " + playerRounds[1] + " " + totalRounds);
            calculateScore(row,col,ones,twos,threes,fours,fives,sixes);
            playerTurn=1;
            label.setText("Player 1's turn");
          }

          boolean allEmpty = false;
          boolean empty=false;
          
          for(int i=1;i<3;i++){
            for(int j = 0; j<6;j++){
              if(table.getValueAt(j,i)==""){
                empty = true;
                break;
              }
            }
          }
          
          for(int i=1;i<3;i++){
            for(int j = 0; j<table.getRowCount();j++){
              if(table.getValueAt(j,i)==""){
                allEmpty = true;
                break;
              }
            }
          }

          if(!empty && filled==0){
            calculateBonuses();
            filled++;
          }
          
          if(!allEmpty){
            calculateTotal();
            if(total1>total2){
              label.setText("Player 1 Wins!");
            }else if(total2>total1){
              label.setText("Player 2 Wins!");
            }else if(total1==total2){
              label.setText("Tie!");
            }
            label.setForeground(Color.RED);
            contentPane3.add(replay);
          }
          reroll.setText("Roll");
          reroll.setActionCommand("Roll");
          roll=0;
        }
      }
    });
    

    frame.setContentPane(contentPane);
    frame.pack();
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    frame.setVisible(true);

  };

  public void actionPerformed(ActionEvent event) {
    String eventName = event.getActionCommand();
    if (eventName.equals("Dice1")){
      changeColor(dice1);
    }else if (eventName.equals("Dice2")){
      changeColor(dice2);
    }else if (eventName.equals("Dice3")){
      changeColor(dice3);
    }else if (eventName.equals("Dice4")){
      changeColor(dice4);
    }else if (eventName.equals("Dice5")){
      changeColor(dice5);
    }
    
    if (eventName.equals("Reroll") && roll<3){
      if(dice1.getBackground().equals(Color.GREEN)){
        dice1=decideButton(1,diceNums,dice1);
      }
      if(dice2.getBackground().equals(Color.GREEN)){
        dice2=decideButton(2,diceNums,dice2);
      }
      if(dice3.getBackground().equals(Color.GREEN)){
        dice3=decideButton(3,diceNums,dice3);
      }
      if(dice4.getBackground().equals(Color.GREEN)){
        dice4=decideButton(4,diceNums,dice4);
      }
      if(dice5.getBackground().equals(Color.GREEN)){
        dice5=decideButton(5,diceNums,dice5);
      }
      
      roll++;

      if(roll==3){
        reroll.setText("Done, fill in the table");
        reroll.setActionCommand("Done");
      }
      
    }else if(eventName.equals("Roll") && roll==0){
      dice1=decideButton(1,diceNums,dice1);
      dice2=decideButton(2,diceNums,dice2);
      dice3=decideButton(3,diceNums,dice3);
      dice4=decideButton(4,diceNums,dice4);
      dice5=decideButton(5,diceNums,dice5);
      dice1.setBackground(Color.GREEN);
      dice2.setBackground(Color.GREEN);
      dice3.setBackground(Color.GREEN);
      dice4.setBackground(Color.GREEN);
      dice5.setBackground(Color.GREEN);
      reroll.setText("Reroll");
      reroll.setActionCommand("Reroll");
      roll++;

    }

    if(eventName.equals("Replay")){
      dice1=decideButton(1,diceNums,dice1);
      dice2=decideButton(2,diceNums,dice2);
      dice3=decideButton(3,diceNums,dice3);
      dice4=decideButton(4,diceNums,dice4);
      dice5=decideButton(5,diceNums,dice5);
      reroll.setText("Reroll");
      reroll.setActionCommand("Reroll");
      roll=1;
      total1=0;
      total2=0;
      bonus1=0;
      bonus2=0;
      playerRounds[0]=0;
      playerRounds[1]=0;
      totalRounds=0;
      upTotal1=0;
      downTotal1=0;
      upTotal2=0;
      downTotal2=0;
      total1=0;
      total2=0;
      label.setText("Player 1's turn");
      label.setForeground(Color.BLACK);
      contentPane3.remove(replay);
      filled=0;
      model.removeRow(6);
      model.removeRow(13);
      for(int i=0;i<13;i++){
        table.setValueAt("",i,1);
        table.setValueAt("",i,2);
      }
    }

  }

  public JButton decideButton(int dice, int[] diceNums, JButton button){
    int num = (int)(Math.random()*6)+1;
    diceNums[dice-1]=num;
    
    if(num==1){
      button.setIcon(new ImageIcon("face_1.png"));
    }else if(num==2){
      button.setIcon(new ImageIcon("face_2.png"));
    }else if(num==3){
      button.setIcon(new ImageIcon("face_3.png"));
    }else if(num==4){
      button.setIcon(new ImageIcon("face_4.png"));
    }else if(num==5){
      button.setIcon(new ImageIcon("face_5.png"));
    }else{
      button.setIcon(new ImageIcon("face_6.png"));
    }
    button.setBackground(Color.GREEN);
    button.setActionCommand("Dice"+dice);
    return button;
  }

  public void changeColor(JButton button){
    if(button.getBackground().equals(Color.RED)){
      button.setBackground(Color.GREEN);
    }else{
      button.setBackground(Color.RED);
    }
  }

  public void calculateScore(int row, int col, int ones, int twos, int threes, int fours, int fives, int sixes){
    
    ArrayList<Integer> tempNums = new ArrayList<Integer>();
    for (int num : diceNums) {
      tempNums.add(num);
    }
    sortList(tempNums);
    findAndRemoveDuplicates(tempNums);
        
    if(table.getValueAt(row,0).equals("Ones")){
      table.setValueAt(String.valueOf(ones),row,col);
    }else if(table.getValueAt(row,0).equals("Twos")){
      table.setValueAt(String.valueOf(twos*2),row,col);
    }else if(table.getValueAt(row,0).equals("Threes")){
      table.setValueAt(String.valueOf(threes*3),row,col);
    }else if(table.getValueAt(row,0).equals("Fours")){
      table.setValueAt(String.valueOf(fours*4),row,col);
    }else if(table.getValueAt(row,0).equals("Fives")){
      table.setValueAt(String.valueOf(fives*5),row,col);
    }else if(table.getValueAt(row,0).equals("Sixes")){
      table.setValueAt(String.valueOf(sixes*6),row,col);
    }else if(table.getValueAt(row,0).equals("Three of a Kind")){
      if(ones>=3 || twos>=3 || threes>=3 || fours>=3 || fives>=3 || sixes>=3){
        table.setValueAt(String.valueOf(diceNums[0]+diceNums[1]+diceNums[2]+diceNums[3]+diceNums[4]),row,col);
      }else{
        table.setValueAt(String.valueOf(0),row,col);
      }
    }else if(table.getValueAt(row,0).equals("Four of a Kind")){
      if(ones>=4 || twos>=4 || threes>=4 || fours>=4 || fives>=4 || sixes>=4){
        table.setValueAt(String.valueOf(diceNums[0]+diceNums[1]+diceNums[2]+diceNums[3]+diceNums[4]),row,col);
      }else{
        table.setValueAt(String.valueOf(0),row,col);
      }
    }else if(table.getValueAt(row,0).equals("Full House")){
      if(ones==3&&twos==2||ones==3&&threes==2||ones==3&&fours==2||ones==3&&fives==2||ones==3&&sixes==2||twos==3&&ones==2||twos==3&&threes==2||twos==3&&fours==2||twos==3&&fives==2||twos==3&&sixes==2||threes==3&&ones==2||threes==3&&twos==2||threes==3&&fours==2||threes==3&&fives==2||threes==3&&sixes==2||fours==3&&ones==2||fours==3&&twos==2||fours==3&&threes==2||fours==3&&fives==2||fours==3&&sixes==2||fives==3&&ones==2||fives==3&&twos==2||fives==3&&threes==2||fives==3&&fours==2||fives==3&&sixes==2||sixes==3&&ones==2||sixes==3&&twos==2||sixes==3&&threes==2||sixes==3&&fours==2||sixes==3&&fives==2){
        table.setValueAt(String.valueOf(25),row,col);
      }else{
        table.setValueAt(String.valueOf(0),row,col);
      }
    }else if(table.getValueAt(row,0).equals("Small Straight")){
      if(tempNums.size()>=4&&(tempNums.get(0)==tempNums.get(1)-1&&tempNums.get(1)==tempNums.get(2)-1 && tempNums.get(2)==tempNums.get(3)-1) || tempNums.size()==5 &&tempNums.get(1)==tempNums.get(2)-1 && tempNums.get(2)==tempNums.get(3)-1 && tempNums.get(3)==tempNums.get(4)-1){
        table.setValueAt(String.valueOf(30),row,col);
      }else{
        table.setValueAt(String.valueOf(0),row,col);
      }
      
    }else if(table.getValueAt(row,0).equals("Large Straight")){
      sortList(tempNums);
      if(ones==1&&twos==1&&threes==1&&fours==1&&fives==1|| twos==1&&threes==1&&fours==1&&fives==1&&sixes==1){
        table.setValueAt(String.valueOf(40),row,col);
      }else{
        table.setValueAt(String.valueOf(0),row,col);
      }
    }else if(table.getValueAt(row,0).equals("Chance")){
      table.setValueAt(String.valueOf(diceNums[0]+diceNums[1]+diceNums[2]+diceNums[3]+diceNums[4]),row,col);
    }else if(table.getValueAt(row,0).equals("Yahtzee")){
      if(diceNums[0]==diceNums[1]&&diceNums[1]==diceNums[2]&&diceNums[2]==diceNums[3]&&diceNums[3]==diceNums[4]){
        if(table.getValueAt(row,col)==""){
          table.setValueAt(50, row, col);
        }else if(Integer.valueOf(String.valueOf(table.getValueAt(row,col)))>0){
          table.setValueAt(String.valueOf(Integer.valueOf(String.valueOf(table.getValueAt(row,col)))+100), row, col);
        }
      }else{
        table.setValueAt(String.valueOf(0),row,col);
      }
    }

  }

  public void calculateBonuses(){
    upTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(0, 1)));
    upTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(1, 1)));
    upTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(2, 1)));
    upTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(3, 1)));
    upTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(4, 1)));
    upTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(5, 1)));

    upTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(0, 2)));
    upTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(1, 2)));
    upTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(2, 2)));
    upTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(3, 2)));
    upTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(4, 2)));
    upTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(5, 2)));

    if(upTotal1>=63){
      bonus1=35;
    }else{
      bonus1=0;
    }

    if(upTotal2>=63){
      bonus2=35;
    }else{
      bonus2=0;
    }

    model.insertRow(6,new String[]{"Bonus", String.valueOf(bonus1), String.valueOf(bonus2)});
  }

  public void calculateTotal(){
    downTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(7, 1)));
    downTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(8, 1)));
    downTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(9, 1)));
    downTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(10, 1)));
    downTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(11, 1)));
    downTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(12, 1)));
    downTotal1+=Integer.valueOf(String.valueOf(table.getValueAt(13, 1)));
    downTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(7, 2)));
    downTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(8, 2)));
    downTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(9, 2)));
    downTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(10, 2)));
    downTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(11, 2)));
    downTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(12, 2)));
    downTotal2+=Integer.valueOf(String.valueOf(table.getValueAt(13, 2)));

    total1=upTotal1+downTotal1+bonus1;
    total2=upTotal2+downTotal2+bonus2;
    
    model.addRow(new String[]{"Total", String.valueOf(total1), String.valueOf(total2)});
  }

  public void sortList(ArrayList<Integer> list){
    for(int i=0; i<list.size()-1;i++){
      int k = i;
      for(int j=i+1;j<list.size();j++){
        if(list.get(j)<list.get(k)){
          k=j;
        }
      }
      int temp = list.get(k);
      list.set(k,list.get(i));
      list.set(i,temp);
    }
  }

  public void findAndRemoveDuplicates(ArrayList<Integer> list){
    for(int i=0;i<list.size();i++){
      for(int j=i+1;j<list.size();j++){
        if(list.get(i)==list.get(j)){
          list.remove(j);
          j--;
        }
      }
    }
  }


  public static void runGUI() {
    JFrame.setDefaultLookAndFeelDecorated(true);
    Yahtzee yahtzee = new Yahtzee();
  }
}