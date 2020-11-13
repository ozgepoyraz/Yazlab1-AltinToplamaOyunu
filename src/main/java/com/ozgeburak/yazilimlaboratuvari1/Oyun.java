package com.ozgeburak.yazilimlaboratuvari1;

import com.ozgeburak.yazilimlaboratuvari1.AStar.Node;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class Oyun extends JPanel {
    //Sabitler üzerinden hesaplanan değişkenler ve yeni sabitler
    public static int pencereGenislik = 1280;
    public static int pencereYukseklik = 720;
    public static final int TAHTA_GENISLIK = Sabitler.HARITA_SUTUN * Sabitler.KARE_GENISLIK;
    public static final int TAHTA_YUKSEKLIK = Sabitler.HARITA_SATIR * Sabitler.KARE_YUKSEKLIK;

    //Değişkenler 
    private Harita harita;
    private OyuncuA oyuncuA;
    private OyuncuB oyuncuB;
    private OyuncuC oyuncuC;
    private OyuncuD oyuncuD;
    private JSlider oyunHizi;
    private int tur;

    public Oyun() {
        //tanımlamalar,atamalar ve ayarlamalar ayarla metodunda yapiliyor.
    }

    public void ayarla() {
        //Nesneler oluşturuluyor.
        harita = new Harita(Sabitler.HARITA_SUTUN, Sabitler.HARITA_SATIR);
        oyuncuA = new OyuncuA(0, 0);
        oyuncuB = new OyuncuB(harita.yatayKareSayisi - 1, 0);
        oyuncuC = new OyuncuC(0, harita.dikeyKareSayisi - 1);
        oyuncuD = new OyuncuD(harita.yatayKareSayisi - 1, harita.dikeyKareSayisi - 1);

        oyunHizi = new JSlider(1, 1000, 1);
        oyunHizi.setValue(300);
        
        //Slider pozisyonu ayarlanacak -------------------------------------------------------------------------------------------------------------------------------------------
        oyunHizi.setBounds(Sabitler.OYUN_GENISLIK - 120, Sabitler.OYUN_YUKSEKLIK - 60, 120, 30);
        this.add(oyunHizi);
        
        //Arkaplan
        this.setBackground(new Color(141,183,242));
    }

    public void guncelle() throws InterruptedException {
        //OYNANİS
        if (tur % 4 == 0) {
            //Tur başı beklemesi
            Thread.sleep(oyunHizi.getValue());

            //A OYUNCUSU
            System.out.println("A oynuyor");
            if (oyuncuA != null) {

                //Oyunun En başında hedefi yoksa bir kere hedef belirleyecek.
                if (oyuncuA.mevcutHedefVarMi == false) {

                    oyuncuA.hedefBelirle(harita);
                    oyuncuA.altin -= 15;

                    if (oyuncuA.altin <= 0) {
                        oyuncuA = null;
                    }

                    Thread.sleep(oyunHizi.getValue());

                }

                //Mevcut bir hedefi var ve ilerliyor.
                //Artık oyuncuA her tur ilerleyerek hedefe gidebilir 3 kare ilerleyerek
                for (int i = 0; i < oyuncuA.kalanHareket; i++) {
                    //Hareket etmeden önce hedefledigimiz altinin hala mevcut olup olmadigina bakacagiz
                    // eger hedefledigimiz altin hala mevcut degilse yeniden hedef belirleyeceğiz

                    if (oyuncuA.hedefAltin != null) {
                        if (oyuncuA.hedefAltin.altin == false) {
                            oyuncuA.hedefBelirle(harita);
                            Thread.sleep(oyunHizi.getValue());
                        }
                    }

                    //Hareket ediyor
                    if (oyuncuA.mevcutHedefVarMi == true && oyuncuA.hedefYol.size() > 0) {
                        oyuncuA.koordinatX = oyuncuA.hedefYol.get(0).x;
                        oyuncuA.koordinatY = oyuncuA.hedefYol.get(0).y;
                        oyuncuA.hedefYol.remove(0);
                        this.repaint();
                        Thread.sleep(oyunHizi.getValue() * 3);
                    }

                    //Her hamlede hamle maaliyeti kadar azalcak constant
                    //oyuncuA.altin -= 5;
                    //Her hareketten sonra altini aldi mi diye bakacagiz
                    if (oyuncuA.koordinatX == oyuncuA.hedefkare.x && oyuncuA.koordinatY == oyuncuA.hedefkare.y) {
                        //Ayni yerdeyse altini alacak ve hareket etmeyecek.
                        for (Kare kare : harita.kareler) {
                            if (kare.koordinatX == oyuncuA.koordinatX && kare.koordinatY == oyuncuA.koordinatY) {
                                System.out.println("Alinan Altin Miktari " + kare.altinMiktari);
                                oyuncuA.altin += kare.altinMiktari;
                                kare.altin = false;
                                //altin miktarini oyuncuya ekleyecegiz
                                Thread.sleep(oyunHizi.getValue());
                                break;
                            }
                        }
                        oyuncuA.mevcutHedefVarMi = false;
                        oyuncuA.hedefYol = null;
                        oyuncuA.hedefkare = null;
                        //Altini da listeden silecegiz.
                        System.out.println("altin aldi");

                        //Bu Noktadan sonra altını aldı ve hedefsiz kaldı
                        if (oyuncuA.mevcutHedefVarMi == false) {
                            oyuncuA.hedefBelirle(harita);
                            oyuncuA.altin -= 15;
                            if (oyuncuA.altin <= 0) {
                                oyuncuA = null;
                            }
                            Thread.sleep(oyunHizi.getValue());
                        }

                        //Altını aldığında mevcut hedefi kalmıyor
                        this.repaint();
                        Thread.sleep(oyunHizi.getValue() * 3);
                        break;

                    }

                }

            }

            tur++;
            //}
        } else if (tur % 4 == 1) {
            //B Oyuncusu
            Thread.sleep(oyunHizi.getValue());

            System.out.println("B oynuyor");
            //Oyunun En başında hedefi yoksa bir kere hedef belirleyecek.
            if (oyuncuB.mevcutHedefVarMi == false) {

                oyuncuB.maaliyetliHedefBelirle(harita, Sabitler.OYUNCU_B_HAMLE_MAALIYET, Sabitler.OYUNCU_B_HEDEF_BELIRLEME_MAALIYET);
                oyuncuB.altin -= 10;
                Thread.sleep(oyunHizi.getValue());

            }

            //Mevcut bir hedefi var ve ilerliyor.
            //Artık oyuncuA her tur ilerleyerek hedefe gidebilir 3 kare ilerleyerek
            for (int i = 0; i < oyuncuB.kalanHareket; i++) {
                if (oyuncuB.hedefAltin != null) {
                    if (oyuncuB.hedefAltin.altin == false) {
                        oyuncuB.maaliyetliHedefBelirle(harita, Sabitler.OYUNCU_B_HAMLE_MAALIYET, Sabitler.OYUNCU_B_HEDEF_BELIRLEME_MAALIYET);
                        Thread.sleep(oyunHizi.getValue());
                    }
                }

                //Hareket ediyor
                if (oyuncuB.mevcutHedefVarMi == true && oyuncuB.hedefYol.size() > 0) {
                    oyuncuB.koordinatX = oyuncuB.hedefYol.get(0).x;
                    oyuncuB.koordinatY = oyuncuB.hedefYol.get(0).y;
                    oyuncuB.hedefYol.remove(0);
                    this.repaint();
                    Thread.sleep(oyunHizi.getValue() * 3);
                }

                //Her hareketten sonra altini aldi mi diye bakacagiz
                if (oyuncuB.koordinatX == oyuncuB.hedefkare.x && oyuncuB.koordinatY == oyuncuB.hedefkare.y) {
                    //Ayni yerdeyse altini alacak ve hareket etmeyecek.
                    for (Kare kare : harita.kareler) {
                        if (kare.koordinatX == oyuncuB.koordinatX && kare.koordinatY == oyuncuB.koordinatY) {
                            oyuncuB.altin += kare.altinMiktari;
                            kare.altin = false;
                            Thread.sleep(oyunHizi.getValue());
                            break;
                        }
                    }
                    oyuncuB.mevcutHedefVarMi = false;
                    oyuncuB.hedefYol = null;
                    oyuncuB.hedefkare = null;
                    //Altini da listeden silecegiz.
                    System.out.println("altin aldi");

                    //altini aldiysak
                    if (oyuncuB.mevcutHedefVarMi == false) {
                        oyuncuB.maaliyetliHedefBelirle(harita, Sabitler.OYUNCU_B_HAMLE_MAALIYET, Sabitler.OYUNCU_B_HEDEF_BELIRLEME_MAALIYET);
                        oyuncuB.altin -= 15;
                        Thread.sleep(oyunHizi.getValue());
                    }

                    //Altını aldığında mevcut hedefi kalmıyor
                    this.repaint();
                    Thread.sleep(oyunHizi.getValue() * 3);
                    break;

                }

            }

            tur++;

        } else if (tur % 4 == 2) {
            //C Oyuncusu
            Thread.sleep(oyunHizi.getValue());
            oyuncuC.gizliAltinlariAcigaCikart(harita, 2);
            Thread.sleep(oyunHizi.getValue());

            System.out.println("C oynuyor");
            //Oyunun En başında hedefi yoksa bir kere hedef belirleyecek.
            if (oyuncuC.mevcutHedefVarMi == false) {

                oyuncuC.maaliyetliHedefBelirle(harita, Sabitler.OYUNCU_C_HAMLE_MAALIYET, Sabitler.OYUNCU_C_HEDEF_BELIRLEME_MAALIYET);
                oyuncuC.altin -= 15;
                Thread.sleep(oyunHizi.getValue());

            }

            //Mevcut bir hedefi var ve ilerliyor.
            //Artık oyuncuA her tur ilerleyerek hedefe gidebilir 3 kare ilerleyerek
            for (int i = 0; i < oyuncuC.kalanHareket; i++) {
                if (oyuncuC.hedefAltin != null) {
                    if (oyuncuC.hedefAltin.altin == false) {
                        oyuncuC.maaliyetliHedefBelirle(harita, Sabitler.OYUNCU_C_HAMLE_MAALIYET, Sabitler.OYUNCU_C_HEDEF_BELIRLEME_MAALIYET);
                        Thread.sleep(oyunHizi.getValue());
                    }
                }

                //Hareket ediyor
                if (oyuncuC.mevcutHedefVarMi == true && oyuncuC.hedefYol.size() > 0) {
                    oyuncuC.koordinatX = oyuncuC.hedefYol.get(0).x;
                    oyuncuC.koordinatY = oyuncuC.hedefYol.get(0).y;
                    oyuncuC.hedefYol.remove(0);
                    this.repaint();
                    Thread.sleep(oyunHizi.getValue() * 3);
                }

                //Her hareketten sonra altini aldi mi diye bakacagiz
                if (oyuncuC.koordinatX == oyuncuC.hedefkare.x && oyuncuC.koordinatY == oyuncuC.hedefkare.y) {
                    //Ayni yerdeyse altini alacak ve hareket etmeyecek.
                    for (Kare kare : harita.kareler) {
                        if (kare.koordinatX == oyuncuC.koordinatX && kare.koordinatY == oyuncuC.koordinatY) {
                            oyuncuC.altin += kare.altinMiktari;
                            kare.altin = false;
                            Thread.sleep(oyunHizi.getValue());
                            break;
                        }
                    }
                    oyuncuC.mevcutHedefVarMi = false;
                    oyuncuC.hedefYol = null;
                    oyuncuC.hedefkare = null;
                    //Altini da listeden silecegiz.
                    System.out.println("altin aldi");

                    //altini aldiysak
                    if (oyuncuC.mevcutHedefVarMi == false) {
                        oyuncuC.maaliyetliHedefBelirle(harita, Sabitler.OYUNCU_C_HAMLE_MAALIYET, Sabitler.OYUNCU_C_HEDEF_BELIRLEME_MAALIYET);
                        oyuncuC.altin -= 15;
                        Thread.sleep(oyunHizi.getValue());
                    }

                    //Altını aldığında mevcut hedefi kalmıyor
                    this.repaint();
                    Thread.sleep(oyunHizi.getValue() * 3);
                    break;

                }

            }

            tur++;

        } else if (tur % 4 == 3) {
            //D Oyuncusu
            Thread.sleep(oyunHizi.getValue());

            System.out.println("D oynuyor");
            //Oyunun En başında hedefi yoksa bir kere hedef belirleyecek.
            if (oyuncuD.mevcutHedefVarMi == false) {

                oyuncuD.sezgiselMaaliyetliHedefBelirle(harita, oyuncuA, oyuncuB, oyuncuC);
                oyuncuD.altin -= 20;
                Thread.sleep(oyunHizi.getValue());

            }

            //Mevcut bir hedefi var ve ilerliyor.
            //Artık oyuncuA her tur ilerleyerek hedefe gidebilir 3 kare ilerleyerek
            for (int i = 0; i < oyuncuD.kalanHareket; i++) {
                if (oyuncuD.hedefAltin != null) {
                    if (oyuncuD.hedefAltin.altin == false) {
                        oyuncuD.sezgiselMaaliyetliHedefBelirle(harita, oyuncuA, oyuncuB, oyuncuC);
                        Thread.sleep(oyunHizi.getValue());
                    }
                }

                //Hareket ediyor
                if (oyuncuD.mevcutHedefVarMi == true && oyuncuD.hedefYol.size() > 0) {
                    oyuncuD.koordinatX = oyuncuD.hedefYol.get(0).x;
                    oyuncuD.koordinatY = oyuncuD.hedefYol.get(0).y;
                    oyuncuD.hedefYol.remove(0);
                    this.repaint();
                    Thread.sleep(oyunHizi.getValue() * 3);
                }

                //Her hareketten sonra altini aldi mi diye bakacagiz
                if (oyuncuD.koordinatX == oyuncuD.hedefkare.x && oyuncuD.koordinatY == oyuncuD.hedefkare.y) {
                    //Ayni yerdeyse altini alacak ve hareket etmeyecek.
                    for (Kare kare : harita.kareler) {
                        if (kare.koordinatX == oyuncuD.koordinatX && kare.koordinatY == oyuncuD.koordinatY) {
                            oyuncuD.altin += kare.altinMiktari;
                            kare.altin = false;
                            Thread.sleep(oyunHizi.getValue());
                            break;
                        }
                    }
                    oyuncuD.mevcutHedefVarMi = false;
                    oyuncuD.hedefYol = null;
                    oyuncuD.hedefkare = null;
                    //Altini da listeden silecegiz.
                    System.out.println("altin aldi");

                    //altini aldiysak
                    if (oyuncuD.mevcutHedefVarMi == false) {
                        oyuncuD.sezgiselMaaliyetliHedefBelirle(harita, oyuncuA, oyuncuB, oyuncuC);
                        oyuncuD.altin -= 15;
                        Thread.sleep(oyunHizi.getValue());
                    }

                    //Altını aldığında mevcut hedefi kalmıyor
                    this.repaint();
                    Thread.sleep(oyunHizi.getValue() * 3);
                    break;

                }

            }

            tur++;

        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Oyun tahtasını çoklu çözünürlüklü hale getirme ve pencereye ortalama
        float kenarlik = 0.9f;
        float olcekX = (float)pencereYukseklik * kenarlik / (float)TAHTA_GENISLIK;
        float olcekY = (float)pencereYukseklik * kenarlik / (float)TAHTA_YUKSEKLIK;
        int kaymaX = (int)((float)pencereGenislik - ((float)TAHTA_GENISLIK * olcekX) )/2;
        int kaymaY = (int)((float)pencereYukseklik - ((float)TAHTA_YUKSEKLIK * olcekY) )/2;
        
        g2d.translate(kaymaX, kaymaY);
        g2d.scale(olcekX, olcekY);
        
        //Elemanların çizdirilmesi
        harita.Cizdir(g2d);
        oyuncuA.Cizdir(g2d);
        oyuncuA.HedefCizdir(g2d);
        
        oyuncuB.Cizdir(g2d);
        oyuncuB.HedefCizdir(g2d);
        
        oyuncuC.Cizdir(g2d);
        oyuncuC.HedefCizdir(g2d);
        
        oyuncuD.Cizdir(g2d);
        oyuncuD.HedefCizdir(g2d);
        
        harita.altinCizdir(g2d);
        
        oyuncuA.YolCizdir(g2d);
        oyuncuB.YolCizdir(g2d);
        oyuncuC.YolCizdir(g2d);
        oyuncuD.YolCizdir(g2d);
        
        //Diğer elemanların düzgün çizdirilmesi için ölçeği eski haline getiriyoruz.
        g2d.scale(1 / olcekX, 1 / olcekY);
        g2d.translate(-kaymaX, -kaymaY);
        
        BilgiGoster(g2d);
    }

    public void BilgiGoster(Graphics2D g) {
        //Bilgileri ekrana çizen fonksiyon
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial",Font.PLAIN, 15));
        g.drawString("Oyuncu A Altin: " + oyuncuA.altin, pencereGenislik - 200, 20);
        g.drawString("Oyuncu B Altin: " + oyuncuB.altin, pencereGenislik - 200, 40);
        g.drawString("Oyuncu C Altin: " + oyuncuC.altin, pencereGenislik - 200, 60);
        g.drawString("Oyuncu D Altin: " + oyuncuD.altin, pencereGenislik - 200, 80);
    }

}
