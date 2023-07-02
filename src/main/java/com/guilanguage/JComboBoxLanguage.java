package com.guilanguage;

import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

@SuppressWarnings({ "serial", "rawtypes" })

public class JComboBoxLanguage extends JComboBox {

	static Map<String, String> translations;

	List<JGuiLanguage> lista;

	private ImageIcon[] items = {

			new ImageIcon(getClass().getResource("/imgs/flags/1.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/2.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/3.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/4.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/5.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/6.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/7.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/8.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/9.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/10.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/11.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/12.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/13.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/14.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/15.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/16.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/17.png")),

			new ImageIcon(getClass().getResource("/imgs/flags/18.png"))

	};

	public static String saberSeparador() {

		if (System.getProperty("os.name").contains("indows")) {

			return "\\";

		}

		else {

			return "/";

		}

	}

	public static String rutaActual() throws IOException {

		return new File(".").getCanonicalPath() + saberSeparador();

	}

	public static void crearCarpeta(String path) {

		new File(path).mkdir();

	}

	public static List<String> directorio(String ruta, String extension, boolean carpeta) {

		LinkedList<String> lista = new LinkedList<>();

		File f = new File(ruta);

		ArrayList<String> videosPermitidos = new ArrayList<>();

		videosPermitidos.add("mp4");

		videosPermitidos.add("mpg");

		videosPermitidos.add("avi");

		videosPermitidos.add("mkv");

		ArrayList<String> imagenesPermitidas = new ArrayList<>();

		videosPermitidos.add("jpg");

		videosPermitidos.add("png");

		videosPermitidos.add("jpeg");

		videosPermitidos.add("gif");

		if (f.exists()) {

			File[] ficheros = f.listFiles();

			String fichero = "";

			String extensionArchivo;

			File folder;

			for (int x = 0; x < ficheros.length; x++) {

				fichero = ficheros[x].getName();

				folder = new File(ruta + fichero);

				extensionArchivo = extraerExtension(fichero);

				if ((carpeta && folder.isDirectory())
						|| (!carpeta && (folder.isFile() && (extension.equals(".") || extension.equals(extensionArchivo)
								|| (extension.equals("videos") && videosPermitidos.contains(extension))
								|| (extension.equals("images") && !extensionArchivo.equals("gif")
										&& imagenesPermitidas.contains(extension))
								|| (extension.equals("allimages") && imagenesPermitidas.contains(extension)))))) {

					lista.add(ruta + fichero);

				}

			}

		}

		Collections.sort(lista);

		return lista;

	}

	public static String extraerExtension(String nombreArchivo) {

		String extension = "";

		try {

			if (nombreArchivo.length() >= 3) {

				extension = nombreArchivo.substring(
						nombreArchivo.length() - nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).length());

			}

		}

		catch (Exception e) {

		}

		return extension.toLowerCase();

	}

	static void read() throws IOException {

		crearCarpeta(rutaActual() + "Languages");

		LinkedList<String> archivos = (LinkedList<String>) directorio(rutaActual() + "Languages" + saberSeparador(),
				"po", false);

		String texto = "";

		String idiomaDefault = "";

		FileReader flE = null;

		BufferedReader fE = null;

		String cabecera = "";

		String traduccion = "";

		String textoTraducido = "";

		for (int i = 0; i < archivos.size(); i++) {

			try {

				flE = new FileReader(archivos.get(i));

				fE = new BufferedReader(flE);

				texto = fE.readLine();

				while (texto != null) {

					if (texto.contains("#: fmain.")) {

						try {

							cabecera = texto.substring(9, texto.length());

							texto = fE.readLine();

							idiomaDefault = texto;

							idiomaDefault = idiomaDefault.substring(7, idiomaDefault.length() - 1);

							texto = fE.readLine();

							traduccion = texto;

							traduccion = traduccion.substring(8, traduccion.length() - 1);

							if (traduccion.isEmpty()) {

								textoTraducido = idiomaDefault;
							}

							else {

								textoTraducido = traduccion;

							}

						}

						catch (Exception e) {

						}

						translations.put(archivos.get(i).substring(archivos.get(i).lastIndexOf(saberSeparador()) + 1,
								archivos.get(i).length() - 3) + "." + cabecera, textoTraducido);

					}

					texto = fE.readLine();

				}

				fE.close();

				flE.close();

			}

			catch (Exception e) {

			}

			finally {

				if (fE != null) {

					try {

						fE.close();

					}

					catch (IOException e) {

					}

				}

				if (flE != null) {

					try {

						flE.close();

					}

					catch (IOException e) {

					}

				}

			}

		}

	}

	public String translate(String text) {

		if (translations.isEmpty()) {

			try {

				read();

			}

			catch (IOException e) {

			}

		}

		if (translations == null) {

			return "";

		}

		else {

			return traducirTexto(translations.get(getLanguage() + "." + text));

		}

	}

	private JGuiLanguage getLanguage() {

		return lista.get(getSelectedIndex());

	}

	private static String traducirTexto(String string) {

		try {

			byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

			string = new String(bytes, StandardCharsets.UTF_8);

		}

		catch (Exception e) {

		}

		return string;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public JComboBoxLanguage(JGuiLanguage main, List<JGuiLanguage> list) {

		translations = new HashMap<>();

		if (list == null) {

			list = new LinkedList<>();

			list.add(JGuiLanguage.ENGLISH);

			list.add(JGuiLanguage.ESPAÑOL);

			list.add(JGuiLanguage.DEUTSCH);

			list.add(JGuiLanguage.FRANÇAIS);

			list.add(JGuiLanguage.PORTUGUÊS);

			list.add(JGuiLanguage.عرب);

			list.add(JGuiLanguage.中國人);

			list.add(JGuiLanguage.日本);

		}

		this.lista = list;

		Dimension d = new Dimension(40, 40);

		this.setSize(d);

		this.setPreferredSize(d);

		setLayout(new javax.swing.GroupLayout(this));

		ImageIcon[] idiomas = new ImageIcon[list.size()];

		int indice = 0;

		int indiceSeleccionado = 0;

		if (main == null) {

			main = JGuiLanguage.ENGLISH;

		}

		for (int i = 0; i < list.size(); i++) {

			this.addItem(i);

		}

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).equals(main)) {

				indiceSeleccionado = i;

			}

			switch (list.get(i)) {

			case ESPAÑOL:

				indice = 0;

				break;

			case DEUTSCH:

				indice = 1;

				break;

			case ENGLISH:

				indice = 2;

				break;

			case FRANÇAIS:

				indice = 3;

				break;

			case РУССКИЙ:

				indice = 4;

				break;

			case ITALIANO:

				indice = 5;

				break;

			case PORTUGUÊS:

				indice = 6;

				break;

			case 中國人:

				indice = 7;

				break;

			case HINDI:

				indice = 8;

				break;

			case 日本:

				indice = 9;

				break;

			case CATALÀ:

				indice = 10;

				break;

			case কোরিয়ান:

				indice = 11;

				break;

			case عرب:

				indice = 12;

				break;

			case EUSKARA:

				indice = 13;

				break;

			case 한국어:

				indice = 14;

				break;

			case TIẾNG_VIỆT:

				indice = 15;

				break;

			case POLSKIE:

				indice = 16;

				break;

			case GALEGO:

				indice = 17;

				break;

			}

			idiomas[i] = items[indice];

		}

		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				int valorIndice = getSelectedIndex();

				if (e.getWheelRotation() < 0) {

					valorIndice--;

				}

				else {

					valorIndice++;

				}

				if (valorIndice < 0) {

					valorIndice = 0;

				}

				if (valorIndice >= getItemCount()) {

					valorIndice = getItemCount();

					valorIndice--;

				}

				setSelectedIndex(valorIndice);

			}

		});

		JChomboRenderer render = new JChomboRenderer(idiomas);

		setRenderer(render);

		setSelectedIndex(indiceSeleccionado);

		this.setVisible(true);

	}

}
