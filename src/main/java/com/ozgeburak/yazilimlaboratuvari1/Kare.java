package com.ozgeburak.yazilimlaboratuvari1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import static java.awt.font.TextAttribute.FONT;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;



//Oyundaki birim kareler
//yapısal bir sınıf ve sadece cizdirme metodları bulunuyor
public class Kare {

    int koordinatX;
    int koordinatY;
    int pozisyonX;
    int pozisyonY;
    int genislik;
    int yukseklik;
    boolean baslangicKaresi = false;
    boolean altin = false;
    boolean gizliAltin = false;
    int altinMiktari = 0;

    Kare(int koordinatX, int koordinatY) {
        this.koordinatX = koordinatX;
        this.koordinatY = koordinatY;
        genislik = 128;
        yukseklik = 128;
        this.pozisyonX = koordinatX * genislik;
        this.pozisyonY = koordinatY * yukseklik;

    }

    void Cizdir(Graphics2D g) {

        if ((koordinatX % 2 == 0 && koordinatY % 2 == 0) || (koordinatX % 2 == 1 && koordinatY % 2 == 1)) {
            g.drawImage(Oyun.resimAcik, koordinatX * genislik, koordinatY * yukseklik, null);
        } else {
            g.drawImage(Oyun.resimKoyu, koordinatX * genislik, koordinatY * yukseklik, null);
        }

    }

    void altinCizdir(Graphics2D g) {
        if (altin == true) {
            g.drawImage(Oyun.altinResim, koordinatX * genislik, koordinatY * yukseklik, null);
        } else if (gizliAltin == true) {
            g.drawImage(Oyun.gizliAltinResim, koordinatX * genislik, koordinatY * yukseklik, null);
        }

        if (altin == true || gizliAltin == true) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 35));
            int oteleme;
            if (altinMiktari == 5) {
                oteleme = 10;
            } else {
                oteleme = 20;
            }
            g.drawString(Integer.toString(altinMiktari), pozisyonX + genislik / 2 - oteleme, pozisyonY + yukseklik / 2 + 15);
        }
    }

}
