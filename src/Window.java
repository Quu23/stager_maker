import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Window extends JFrame{

    boolean isMousePointerExited;

    public JPanel drawPanel;
    private JPanel settingPanel;

    private JButton[] settingButtons;

    private int page;

    Window(){
        this.setTitle("stage maker");
        this.setSize(1500, 780);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.isMousePointerExited = true;
        this.page = 0;

        this.drawPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
       this.drawPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch(e.getButton()){
                    //左クリック
                    case MouseEvent.BUTTON1:
                        if(App.nowEntity==EntityKind.NONE)return;
                        App.entities.add(new Entity(e.getX(), e.getY(), App.nowEntity,page));
                        break;
                    case MouseEvent.BUTTON2:
                        App.nowEntity = 0;
                        break;
                    //右クリック 
                    case MouseEvent.BUTTON3:
                        for (int i = App.entities.size() - 1; i >= 0 ; i--) {
                            Entity entity = App.entities.get(i);
                            if(entity.isPointed(e.getX(), e.getY())){
                                App.entities.remove(entity);
                            }
                        }
                        break;
                    default:
                    if(App.nowEntity < EntityKind.SPEED_UP_ITEM){
                        App.nowEntity++;
                        App.tempImage = EntityImages.getImage(App.nowEntity);
                    }else{
                        App.nowEntity = EntityKind.BIG_ENEMY;
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isMousePointerExited = false;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isMousePointerExited = true;
            }
            @Override
            public void mousePressed(MouseEvent e) {
                
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
        });
        this.addMouseWheelListener(e -> {
            //手前に回す
            if(e.getWheelRotation() == 1){
                if(App.nowEntity > 0){
                    App.nowEntity--;
                }else{
                    App.nowEntity = EntityKind.SPEED_UP_ITEM;
                }
                App.tempImage = EntityImages.getImage(App.nowEntity);
            }else if(e.getWheelRotation() == -1){
                if(App.nowEntity < EntityKind.SPEED_UP_ITEM){
                    App.nowEntity++;
                }else{
                    App.nowEntity = EntityKind.BIG_ENEMY;
                }
                App.tempImage = EntityImages.getImage(App.nowEntity);
            }
        });

        this.settingPanel = new JPanel();
        this.settingPanel.setBackground(Color.CYAN);
        
        {
            JButton[] tmp_buttons = {
                new JButton("SAVE"),
                new JButton("LOAD"),
                new JButton("BACK"),
                new JButton("NEXT"),
                new JButton("CLEAR"),
            };
            this.settingButtons = tmp_buttons;
        }
        this.settingButtons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File old = new File("data.txt");
                old.delete();
                File newData = new File("data.txt");

                try{
                    newData.createNewFile();
                }catch(IOException exception){
                    exception.printStackTrace();
                }
                try (Writer saveWriter = new BufferedWriter(new FileWriter(newData));) {
                    //save処理
                    //stage_position(int) , 0 / 1 , enemy_type(EnemyType) / item_type(ItemType) , x (int)  \n
                    List<Entity> saveDatas = new ArrayList<Entity>(App.entities.size());
                    for (Entity entity : App.entities) {
                        saveDatas.add(new Entity(entity));
                    }
                    for (Entity entity : saveDatas) {
                        entity.y += 780 * entity.page;
                    }
                    saveDatas.sort((en1, en2) -> {
                        if (en1.y > en2.y)return 1;
                        if (en1.y < en2.y)return -1;
                        return 0;
                    });
                    for (int i=0;i<saveDatas.size();i++) {
                        Entity entity = saveDatas.get(i);

                        int isEnemy = 0;
                        if(entity.kind >= 10){
                            entity.kind -= 10;
                            isEnemy = 1;
                        }
                        saveWriter.write(entity.y+","+isEnemy+","+entity.kind+","+entity.x+",");
                        if(i<saveDatas.size()-1)saveWriter.write("\n");
                    }
                    saveWriter.flush();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        this.settingButtons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Scanner sc = new Scanner(new File("data.txt"))) {
                    //load処理
                    //stage_position(int) , 0 / 1 , enemy_type(EnemyType) / item_type(ItemType) , x (int)  \n

                    App.entities.clear();

                    sc.useDelimiter(",");

                    while(sc.hasNextLine()){
                        //次の行を読み込み
                        int stagePos = sc.nextInt();
                        int flag = sc.nextInt();
                        int kind = sc.nextInt() + (flag == 1 ? 10 : 0);
                        int x = sc.nextInt();
                        int y = stagePos % 780;
                        int page = (stagePos-y)/780;
                        System.out.println(stagePos);
                        App.entities.add(new Entity(x, y, kind, page));

                        sc.nextLine();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        this.settingButtons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(page > 0)page--;
            }
        });
        this.settingButtons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(page < 7)page++;
            }
        });
        this.settingButtons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = App.entities.size() - 1; i >= 0; i--){
                    if(App.entities.get(i).page == page){
                        App.entities.remove(i);
                    }
                }
            }
        });

        this.add(this.drawPanel,BorderLayout.CENTER);
        this.add(this.settingPanel,BorderLayout.SOUTH);

        for (JButton jButton : this.settingButtons) {
            this.settingPanel.add(jButton);
        }
    }

    private void draw(Graphics g){
        
        g.setColor(Color.black);
        for (int i = 0;i<800;i+=10){
            g.drawLine(0, i, 1500, i);
            for (int j = 0; j < 1500; j+=10) {
                g.drawLine(j, 0, j, 800);
            }
            g.drawString("y="+i, 1450, i);
        }

        if (App.nowEntity != EntityKind.NONE){
            g.drawImage(App.tempImage, (int)App.mousePoint.getX(), (int)App.mousePoint.getY(), null);
        }

        for (Entity entity : App.entities) {
            if (entity.page == this.page){
                g.drawImage(entity.img,entity.x,entity.y,null);
            }
        }

        g.setColor(Color.CYAN);
        g.fillRect(10, 0, 50,30);

        g.setColor(Color.red);
        g.drawString("page:" + page, 10, 10);
        g.drawString(App.mousePoint.x+":"+App.mousePoint.y , 10,20);
        g.drawString(App.nowEntity + "",10,30);
    }


}
