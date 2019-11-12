package calculator;

import java.applet.Applet;
import java.awt.TextField;
import java.awt.Label;
import java.awt.Button;	
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;		
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class CalcApplet extends Applet implements ActionListener {
    TextField valueField[ ] = new TextField[4];
    double value[] = new double[4];
    String str = "";
    String order = "";
    String result = "";
    Button transButton;
    Button creatButton;
    Button dispButton;

    public void init( ) {
        Label label[ ] = new Label[1];	
        label[0] = new Label("中置記法の式");
        this.add(label[0]);
        this.valueField[0] = new TextField( );
        valueField[0].setPreferredSize(new Dimension(500, 30));
        this.add(this.valueField[0]);

        this.transButton = new Button("変換");
        this.add(this.transButton);
        this.transButton.addActionListener(this);
        
        this.creatButton = new Button("生成");
        this.add(this.creatButton);
        this.creatButton.addActionListener(this);
        
        this.dispButton = new Button("実行");
        this.add(this.dispButton);
        this.dispButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Button button = (Button)evt.getSource( );
        if (button == this.dispButton) {
            //実行の処理
            Calc cal = new Calc();
            result = cal.cal(this.order);
            this.repaint();
        }
        else if(button == this.transButton){
            //変換の処理
            this.str = this.valueField[0].getText();
            Node node = new Node(this.str);
            node.mae();
            node.createTree();

            str = node.traceTree();
            if(str.equals("")){
                str = "正しく式を入力してください";
            }
            this.repaint();
        }
        else if(button == this.creatButton){
            //生成の処理
            ReadOder rf = new ReadOder(str);
            order = rf.ReadFormula();
            if(!order.equals("正しく式を入力してください")){
                order += "wrt halt";
            }
            this.repaint();
        }
    }

    public void paint(Graphics g) {
        Font font = new Font("TimesRoman",Font.BOLD,18);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString("後置記法の式",10,70);
        g.drawString(str , 10, 100);
        g.setColor(Color.blue);
        g.drawString("スタックマシンの命令列", 10, 130);
        g.drawString(order, 10, 160);
        g.drawString("実行結果", 10, 190);
        g.drawString(result,10,220);
    }
}
