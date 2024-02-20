package pelota;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rebota extends JFrame {

    // Panel donde se dibujarán las pelotas
    private JPanel panelMarco;

    // Botón para crear nuevas pelotas
    private JButton botonComenzar;

    // Lista para almacenar las pelotas creadas
    private List<Pelota> pelotas;

    // Temporizador para mover las pelotas
    private Timer timer;

    // Constructor de la clase Rebota
    public Rebota() {
        setTitle("Pelota Rebotadora");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicialización del panel
        panelMarco = new JPanel();
        panelMarco.setLayout(null);
        panelMarco.setBorder(BorderFactory.createLineBorder(Color.white));
        add(panelMarco);

        // Inicialización del botón
        botonComenzar = new JButton("Comenzar");
        botonComenzar.setBounds(200, 400, 100, 30);
        panelMarco.add(botonComenzar);

        // Inicialización de la lista de pelotas
        pelotas = new ArrayList<>();

        // Configuración del ActionListener para el botón
        botonComenzar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPelota();
            }
        });

        // Inicialización del temporizador para mover las pelotas
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverPelotas();
            }
        });
        timer.start(); // Inicia el temporizador
    }

    // Método para crear una nueva pelota
    private void crearPelota() {
        Random rand = new Random();
        int pelotaSize = 50;
        int x = rand.nextInt(panelMarco.getWidth() - pelotaSize);
        int y = rand.nextInt(panelMarco.getHeight() - pelotaSize);

        Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        // Crear una nueva pelota y agregarla al panel y a la lista
        Pelota nuevaPelota = new Pelota(x, y, pelotaSize, randomColor, panelMarco.getWidth(), panelMarco.getHeight());
        panelMarco.add(nuevaPelota.getEtiqueta());
        pelotas.add(nuevaPelota);
    }

    // Método para mover todas las pelotas
    private void moverPelotas() {
        for (int i = 0; i < pelotas.size(); i++) {
            Pelota pelota1 = pelotas.get(i);
            pelota1.mover();

            for (int j = i + 1; j < pelotas.size(); j++) {
                Pelota pelota2 = pelotas.get(j);

                // Verificar colisión entre las dos pelotas
                if (pelota1.colisionCon(pelota2)) {
                    // Intercambiar direcciones para simular el rebote
                    int tempDirX = pelota1.direccionX;
                    int tempDirY = pelota1.direccionY;
                    pelota1.direccionX = pelota2.direccionX;
                    pelota1.direccionY = pelota2.direccionY;
                    pelota2.direccionX = tempDirX;
                    pelota2.direccionY = tempDirY;
                }
            }
        }
    }

    // Clase interna que representa una pelota
    private class Pelota {
        private JLabel etiqueta; // Etiqueta para mostrar la pelota
        private int direccionX; // Dirección horizontal de la pelota
        private int direccionY; // Dirección vertical de la pelota
        private int rebotes; // Contador de rebotes

        // Constructor de la clase Pelota
        public Pelota(int x, int y, int size, Color color, int panelWidth, int panelHeight) {
            etiqueta = new JLabel() {
                // Sobrescribe el método paintComponent para personalizar el dibujo de la pelota
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(color);
                    g.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            etiqueta.setBounds(x, y, size, size);
            direccionX = 1;
            direccionY = 1;
            rebotes = 0;
        }

        // Método para obtener la etiqueta de la pelota
        public JLabel getEtiqueta() {
            return etiqueta;
        }

        // Método para mover la pelota
        public void mover() {
            int pelotaX = etiqueta.getX();
            int pelotaY = etiqueta.getY();

            // Verificar colisión con los bordes del panel
            if (pelotaX + direccionX < 0 || pelotaX + direccionX > panelMarco.getWidth() - etiqueta.getWidth()) {
                direccionX = -direccionX; // Invertir la dirección horizontal al tocar un borde
                rebotes++; // Incrementar el contador de rebotes
            }
            if (pelotaY + direccionY < 0 || pelotaY + direccionY > panelMarco.getHeight() - etiqueta.getHeight()) {
                direccionY = -direccionY; // Invertir la dirección vertical al tocar un borde
                rebotes++; // Incrementar el contador de rebotes
            }

            // Eliminar la pelota si ha rebote más de 100 veces
            if (rebotes >= 100) {
                pelotas.remove(this);
                panelMarco.remove(etiqueta);
            }

            // Mover la pelota a la nueva posición
            etiqueta.setLocation(pelotaX + direccionX, pelotaY + direccionY);
        }

        // Método para verificar colisión con otra pelota
        public boolean colisionCon(Pelota otraPelota) {
            Rectangle rect1 = etiqueta.getBounds();
            Rectangle rect2 = otraPelota.etiqueta.getBounds();
            return rect1.intersects(rect2);
        }
    }

    // Método principal
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Rebota().setVisible(true);
            }
        });
    }
}
