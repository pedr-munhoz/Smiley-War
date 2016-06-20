import java.awt.*;
import java.io.*;
import javax.imageio.*;

class Weapon extends Munhoz_Engine{
   Image right, left;
   int x,y,damage,cooldown,height,width,speed;
   boolean automatic, exists=true;
}

class Rifle extends Weapon{
   Rifle(Image i_right, Image i_left){
      damage=4;
      cooldown=80;
      x=10;
      y=25;
      speed=50;
      automatic=false;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }
}

class Assault_Rifle extends Weapon{
   Assault_Rifle(Image i_right, Image i_left){
      x=20;
      y=25;
      damage=1;
      cooldown=15;
      speed=40;
      automatic=true;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }
}

class None extends Weapon{
   None(){
      exists = false;
   }
}

class Revolver extends Weapon{
   Revolver(Image i_right, Image i_left){
      x=25;
      y=25;
      damage=3;
      cooldown=30;
      speed=40;
      automatic=false;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }
}

class Rocket_Louncher extends Weapon{
   Rocket_Louncher(Image i_right, Image i_left){
      x=0;
      y=5;
      damage=40;
      cooldown=100;
      speed=20;
      automatic=false;
      right = i_right;
      height=right.getHeight(this);
      width=right.getWidth(this);
      left = i_left;
   }
}

class Bullet extends Munhoz_Engine{
   Speed speed = new Speed();
   int x,y,damage;
   Player whose= new Player();
   static int bullet_count=0,height,width;
   boolean show=false;
   Image image;
   void setBullet(Image img, int new_x, int new_y, double new_speed, int new_damage){
      image = img;
      height=image.getHeight(this);
      width=image.getWidth(this);
      x=new_x;
      if(new_speed<0)
         x-=width;
      y=new_y+height/2;
      speed.right=new_speed;
      damage = new_damage;
   }
}

class Speed {
   double down, right, ac;
   void recalculate(){
      down+=ac;
   }
}

class Player {
   Image right, left;
   Image[] hp_bar = new Image[4];
   boolean to_right, can_jump, can_shot;
   int x, y, hp, height, width, shotTimer, score=0;
   Speed speed = new Speed();
   Weapon weapon;
}

class Obstacle extends Munhoz_Engine{
   Image image;
   int x, y, width, height;
   Obstacle(int new_x, int new_y, String img_file){
      x=new_x;
      y=new_y;
      try {
        image = ImageIO.read(new File(img_file));
        height=image.getHeight(this);
        width=image.getWidth(this);
      } catch (IOException e) {
        System.out.println("A imagem nao pode ser carregada.");
      }
   }
   Obstacle(int new_x, int new_y, int new_width, int new_height){
      x=new_x;
      y=new_y;
      height=new_height;
      width=new_width;
   }
}

class Weapon_Drop extends Weapon{
   Weapon weapon;
   int x, y, width, height, timer;
   Weapon_Drop (int new_x, int new_y, String str, Image i_right, Image i_left){
      switch(str){
         case "rifle":
         weapon = new Rifle(i_right, i_left);
         break;

         case "assault_rifle":
         weapon = new Assault_Rifle(i_right, i_left);
         break;


         case "rocket_louncher":
         weapon = new Rocket_Louncher(i_right, i_left);
         break;

         case "revolver":
         weapon = new Revolver(i_right, i_left);
      }
      x = new_x;
      y = new_y;
      width = weapon.width;
      height = 30;
   }
}

class Explosion extends Munhoz_Engine{
   Image image;
   static int counter;
   int timer, x, y, width, height;
   boolean show;
   Explosion(){

   }
   Explosion(int new_x, int new_y, Image img){
      x=new_x;
      y=new_y;
      timer=6;
      show=true;
      image = img;
      height=image.getHeight(this);
      width=image.getWidth(this);
   }
}

class Smiley_War extends Munhoz_Engine {
   Image bullet_r,bullet_l,backgroung,rifle_r,assault_rifle_r,revolver_r;
   Image rifle_l,assault_rifle_l,revolver_l,rocket_louncher_r,rocket_louncher_l;
   Image explosion_img;
   int obsCounter, blCounter=5, plCounter=2, dropCounter, explosion_counter=4;
   Obstacle[] obstacles = new Obstacle[20];
   Player[] player = new Player[4];
   Bullet[] bullets = new Bullet[10];
   Weapon_Drop[] drops = new Weapon_Drop[10];
   Explosion[] explosion = new Explosion[4];


   public void init(){
      setSize(1300, 690);

      setObstacles();

      for(int i=0;i<plCounter;i++){
         player[i]=new Player();
      }

      for(int i=0;i<blCounter;i++){
         bullets[i]=new Bullet();
      }

      for(int i=0;i<explosion_counter;i++){
         explosion[i]=new Explosion();
      }

      for(int i=0;i<plCounter;i++){
         try {
            player[i].right = ImageIO.read(new File("assets/blank_1_r.gif"));
            player[i].height = player[i].right.getHeight(this);
            player[i].width = player[i].right.getWidth(this);
         } catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         try {
            player[i].left = ImageIO.read(new File("assets/blank_1_l.gif"));
         } catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }
         for(int j=0;j<4;j++){
            try {
               player[i].hp_bar[j] = ImageIO.read(new File("assets/hp"+(j+1)+".png"));
            } catch (IOException e) {
               System.out.println("A imagem nao pode ser carregada.");
            }
         }
         spawn(i);
         player[i].weapon = new None();

         /*try {
            backgroung = ImageIO.read(new File(""));
         } catch (IOException e) {
            System.out.println("A imagem nao pode ser carregada.");
         }*/
      }

      try {
         assault_rifle_r = ImageIO.read(new File("assets/assault_rifle_right.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }
      try {
         assault_rifle_l = ImageIO.read(new File("assets/assault_rifle_left.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }

      try {
         rifle_r = ImageIO.read(new File("assets/rifle_right.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }
      try {
         rifle_l = ImageIO.read(new File("assets/rifle_left.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }

      try {
         revolver_r = ImageIO.read(new File("assets/revolver_right.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }
      try {
         revolver_l = ImageIO.read(new File("assets/revolver_left.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }

      try {
         rocket_louncher_r = ImageIO.read(new File("assets/rocket_louncher.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }
      try {
         rocket_louncher_l = ImageIO.read(new File("assets/rocket_louncher.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }

      setDrops();

      try {
         bullet_r = ImageIO.read(new File("assets/bulletR.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }
      try {
         bullet_l = ImageIO.read(new File("assets/bulletL.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }

      try {
         explosion_img = ImageIO.read(new File("assets/explosion.gif"));
      }
      catch (IOException e) {
         System.out.println("A imagem nao pode ser carregada.");
      }
   }

   public void setObstacles(){
      obsCounter=17;
      obstacles[0]=new Obstacle((window.width-900)/2,window.height-50,"assets/p900.png");
      obstacles[1]=new Obstacle((window.width-450)/2,window.height-170,"assets/p450.png");
      obstacles[2]=new Obstacle(0,window.height-170,"assets/p200.png");
      obstacles[3]=new Obstacle(window.width-200,window.height-170,"assets/p200.png");
      obstacles[4]=new Obstacle(0,window.height-620,"assets/pv450.png");
      obstacles[5]=new Obstacle(window.width-14,window.height-620,"assets/pv450.png");
      obstacles[6]=new Obstacle(130,window.height-290,"assets/p350.png");
      obstacles[7]=new Obstacle(window.width-480,window.height-290,"assets/p350.png");
      obstacles[8]=new Obstacle(220,window.height-410,"assets/pv120.png");
      obstacles[9]=new Obstacle(window.width-234,window.height-410,"assets/pv120.png");
      obstacles[10]=new Obstacle(150,window.height-410,"assets/p120.png");
      obstacles[11]=new Obstacle(window.width-280,window.height-410,"assets/p120.png");
      obstacles[12]=new Obstacle((window.width-200)/2,window.height-410,"assets/p200.png");
      obstacles[13]=new Obstacle(((window.width-200)/2-270)/2+270,window.height-530,"assets/pv120.png");
      obstacles[14]=new Obstacle(window.width-((window.width-200)/2-270)/2-270,window.height-530,"assets/pv120.png");
      obstacles[15]=new Obstacle(14,window.height-530,"assets/p450.png");
      obstacles[16]=new Obstacle(window.width-450-14,window.height-530,"assets/p450.png");
   }

   void setDrops(){
      dropCounter = 9;
      drops[0]=new Weapon_Drop(window.width/2,window.height-50,"rocket_louncher",rocket_louncher_r,rocket_louncher_l);
      drops[1]=new Weapon_Drop(window.width/2,window.height-170,"rifle",rifle_r,rifle_l);
      drops[2]=new Weapon_Drop(window.width/2,window.height-410,"assault_rifle",assault_rifle_r,assault_rifle_l);
      drops[3]=new Weapon_Drop(50,window.height-170,"revolver",revolver_r,revolver_l);
      drops[4]=new Weapon_Drop(window.width-50,window.height-170,"revolver",revolver_r,revolver_l);
      drops[5]=new Weapon_Drop(210,window.height-410,"revolver",revolver_r,revolver_l);
      drops[6]=new Weapon_Drop(window.width-210,window.height-410,"revolver",revolver_r,revolver_l);
      drops[7]=new Weapon_Drop(300,window.height-530,"revolver",revolver_r,revolver_l);
      drops[8]=new Weapon_Drop(window.width-300,window.height-530,"revolver",revolver_r,revolver_l);
   }

   void spawn(int x){
      switch(x){
         case 0:
         player[x].x=20;
         player[x].to_right=true;
         player[x].y=0;
         break;

         case 1:
         player[x].x=window.width-20-player[1].width;
         player[x].to_right=false;
         player[x].y=0;
         break;

         case 2:
         player[x].x=window.width-20-player[1].width;
         player[x].to_right=false;
         player[x].y=window.height/2;
         break;

         case 3:
         player[x].x=20;
         player[x].to_right=true;
         player[x].y=window.height/2;
      }
      player[x].can_shot=true;
      player[x].speed.ac=0.5;
      player[x].speed.down=0;
      player[x].shotTimer=0;;
      player[x].hp=4;
   }

   void spawn(Player p){
      p.x=window.width/2-p.width/2;
      p.to_right=true;
      p.y=0;
      p.speed.ac=0.5;
      p.speed.down=0;
      p.shotTimer=0;
      p.hp=2;
      p.can_shot=true;
   }

   public double colisionH(Player p, double distance){
      Player aux = new Player();
      aux.x=p.x;
      aux.y=p.y;
      aux.width=p.width;
      aux.height=p.height;

      if(distance+aux.x+aux.width>window.width || distance+aux.x<0){
         if(distance>0){
            return window.width-aux.x-aux.width;
         }
         else
            return -aux.x;
      }
      else
         aux.x+=distance;

      for(int i=0;i<obsCounter;i++){
         if(aux.x>obstacles[i].x-aux.width){
             if(aux.x<obstacles[i].x+obstacles[i].width){
               if(aux.y>obstacles[i].y-aux.height){
                  if(aux.y<obstacles[i].y+obstacles[i].height){
                     return 0;
                  }
               }
            }
         }
      }

      return distance;
   }

   public double colisionV(Player p, double distance){
      Player aux = new Player();
      aux.x=p.x;
      aux.y=p.y;
      aux.width=p.width;
      aux.height=p.height;

      if(distance+aux.y+aux.height>window.height || distance+aux.y<0){
         if(distance+aux.y+aux.height>window.height){
            spawn(p);
            if(p.score>0)
               p.score--;
            }
      }
      else
         aux.y+=distance;

      for(int i=0;i<obsCounter;i++){
         if(aux.x>obstacles[i].x-aux.width){
             if(aux.x<obstacles[i].x+obstacles[i].width){
               if(aux.y>obstacles[i].y-aux.height){
                  if(aux.y<obstacles[i].y+obstacles[i].height){
                     if(distance>0){
                        p.can_jump=true;
                     }
                     return 0;
                  }
               }
            }
         }
      }
      return distance;
   }

   double bullet_movement(Bullet b){
      Bullet aux = new Bullet();
      aux.x=b.x;
      aux.y=b.y;
      aux.width=b.width;
      aux.height=b.height;

      for(int i=0;i<obsCounter;i++){
         if(aux.x>obstacles[i].x-aux.width){
             if(aux.x<obstacles[i].x+obstacles[i].width){
               if(aux.y>obstacles[i].y-aux.height){
                  if(aux.y<obstacles[i].y+obstacles[i].height){
                     if(b.damage==40){
                        explosion[explosion[0].counter]=new Explosion(b.x,b.y, explosion_img);
                        explosion[0].counter++;
                        if(explosion[0].counter>=4)
                           explosion[0].counter=0;
                     }
                     return 0;
                  }
               }
            }
         }
      }

      if(b.speed.right+aux.x+aux.width>window.width || b.speed.right+aux.x<0){
         if(b.speed.right>0){
            return window.width-aux.x-aux.width;
         }
         else{
            return -aux.x;
         }
      }
      else
         aux.x+=b.speed.right;

      for(int i=0;i<obsCounter;i++){
         if(aux.x>obstacles[i].x-aux.width && aux.x<obstacles[i].x+obstacles[i].width && aux.y>obstacles[i].y-aux.height && aux.y<obstacles[i].y+obstacles[i].height){
            if(b.damage==40){
               explosion[explosion[0].counter]=new Explosion(b.x,b.y, explosion_img);
               explosion[0].counter++;
               if(explosion[0].counter>=4)
                  explosion[0].counter=0;
            }
            return 0;
         }
      }
      return b.speed.right;
   }

   public void shot(Player p, Graphics g){
      if(p.to_right){
         bullets[bullets[0].bullet_count].setBullet(bullet_r, p.x+p.weapon.x+p.weapon.width, p.y+p.weapon.y-5,p.weapon.speed,p.weapon.damage);
      }
      else{
         bullets[bullets[0].bullet_count].setBullet(bullet_l, p.x-p.weapon.width+p.width-p.weapon.x, p.y+p.weapon.y-5,-p.weapon.speed,p.weapon.damage);
      }
      bullets[bullets[0].bullet_count].show=true;
      bullets[bullets[0].bullet_count].whose=p;
      bullets[0].bullet_count++;
      if(bullets[0].bullet_count>=blCounter)
         bullets[0].bullet_count=0;
   }

   void hit(Bullet b){
      for(int i=0;i<plCounter;i++){
         if(b.x>player[i].x-b.width){
             if(b.x<player[i].x+player[i].width){
               if(b.y>player[i].y-b.height){
                  if(b.y<player[i].y+player[i].height){
                     player[i].hp-=b.damage;
                     b.show=false;
                     if(player[i].hp<1){
                        if(b.damage==40){
                           explosion[explosion[0].counter]=new Explosion(player[i].x,player[i].y-10, explosion_img);
                           explosion[0].counter++;
                           if(explosion[0].counter>=4)
                              explosion[0].counter=0;
                        }
                        spawn(i);
                        b.whose.score++;
                     }
                  }
               }
            }
         }
      }
   }

   void hit_drop(Player p){
      for(int i=0;i<dropCounter;i++){
         if(drops[i].timer==0){
            if(p.x>(drops[i].x-drops[i].width/2)-p.width && p.x<(drops[i].x-drops[i].width/2)+drops[i].width/2 && p.y>(drops[i].y-drops[i].height)-p.height && p.y<(drops[i].y-drops[i].height)+drops[i].height){
               p.weapon = drops[i].weapon;
               drops[i].timer = 300;
            }
         }
      }
   }

   public void gravity(Player p){
      if(p.speed.down==0)
         p.speed.down=0.5;
      double d = colisionV(p, p.speed.down);
      if(d!=0){
         p.y+=d;
         p.speed.recalculate();
         p.can_jump=false;
      }
      else{
         p.speed.down=0;
      }
   }

   public void paint(Graphics g) {
      double w;
      if(keyboard.keys.contains(54) && player[0].can_jump){
         player[0].speed.down=-12;
         player[0].can_jump=false;
      }
      if(keyboard.keys.contains(90)){
         player[0].to_right=true;
         w = colisionH(player[0], 7);
         player[0].x += w;
      }
      else if(keyboard.keys.contains(55)){
         player[0].to_right=false;
         w = colisionH(player[0], -7);
         player[0].x += w;
      }
      if(keyboard.keys.contains(32) && player[0].shotTimer==0 && player[0].can_shot){
         player[0].shotTimer=player[0].weapon.cooldown;
         player[0].can_shot=false;
      }
      if(!keyboard.keys.contains(' ') || player[0].weapon.automatic){
         player[0].can_shot=true;
      }

      for(int i=0;i<blCounter;i++){
         if(bullets[i].show)
         {
            w = bullet_movement(bullets[i]);
            if(w==0){
               bullets[i].show=false;
            }
            else
               bullets[i].x+=w;
            hit(bullets[i]);
         }
         if(bullets[i].show)
            g.drawImage(bullets[i].image,bullets[i].x,bullets[i].y,this);
      }
      for(int i=0;i<plCounter;i++){
         if(player[i].shotTimer==player[i].weapon.cooldown && player[i].weapon.exists){
            shot(player[i], g);
         }
         if(player[i].shotTimer>0){
            player[i].shotTimer--;
         }
         gravity(player[i]);

         hit_drop(player[i]);

         g.drawString("Player "+(i+1)+": "+player[i].score,20,20+20*i);
         g.drawImage(player[i].hp_bar[player[i].hp-1],player[i].x, player[i].y-20, this);

         if(player[i].to_right){
            g.drawImage(player[i].right, player[i].x, player[i].y, this);
            g.drawImage(player[i].weapon.right,player[i].x+player[i].weapon.x,player[i].y+player[i].weapon.y, this);
         }
         else{
            g.drawImage(player[i].left, player[i].x, player[i].y, this);
            g.drawImage(player[i].weapon.left,player[i].x-player[i].weapon.width+player[i].width-player[i].weapon.x,player[i].y+player[i].weapon.y, this);
         }
      }
      for(int i=0;i<explosion_counter;i++){
         if(explosion[i].show){
            g.drawImage(explosion[i].image,explosion[i].x-explosion[i].width/2,explosion[i].y,this);
            explosion[i].timer--;
         }
         if(explosion[i].timer==0){
            explosion[i].show=false;
         }
      }
      for(int i=0;i<dropCounter;i++){
         if(drops[i].timer==0){
            g.drawImage(drops[i].weapon.right,drops[i].x-drops[i].width/2,drops[i].y-drops[i].height,this);
         }
         else{
            drops[i].timer--;
         }
      }
      for(int i=0;i<obsCounter;i++){
         g.drawImage(obstacles[i].image,obstacles[i].x,obstacles[i].y,this);
      }
      //g.drawImage(backgroung,0,0,this);
      //System.out.println(keyboard.pressed[0]+" "+keyboard.pressed[1]+" "+keyboard.pressed[2]+" "+keyboard.pressed[3]);
   }

   public static void main (String args[]){
      Munhoz_Engine canvas = new Smiley_War();
      new Start(canvas);
   }
}
