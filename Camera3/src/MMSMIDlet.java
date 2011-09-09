/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.wireless.messaging.*;

// Main MIDlet class. It controls the user interface and the
// MMS connection
public class MMSMIDlet
        extends MIDlet
        implements MessageListener, CommandListener, Runnable {

    private final String APPLICATION_ID = "mmsdemo";
    private CameraScreen cameraScreen = null;
    private Camera cameraScreen1 = null;
//  private ReceiveScreen receiveScreen;
//  private SendScreen sendScreen;
//  private InfoScreen infoScreen;
    private Displayable resumeDisplay = null;
    private MessageConnection messageConnection;
    private boolean closing;
    private Message nextMessage = null;
    public Form form;
    public Form form1;
    /*-->*/
    public Command editarencuesta,  exit,  atras,  camera,  guardar,  crear,  editar,  eliminar,  buscar,  transferir,  getMap,  cmdSI,  cmdNO,  Guardar,  Camara,  Mapa;
    public Command Salir,  Salir1,  Buscar,  Buscar1;
    Image img;
    Image mapa;
    /*-->*/
    byte[] pngMap;
    int cont = 0;
    public TextField nombre,  tel,  dir,  buscanombre,  buscanombre1,  nombre1,  tel1,  dir1,  nombre2,  tel2,  dir2;
    public ChoiceGroup intra,  intranet,  acceso,  intra1,  intranet1,  acceso1,  intra2,  intranet2,  acceso2;
    public List lista;
    private Vector dato;
    String nombre11, dir11, intr11, intra11, acces11, tel11;
    //int tel11=0;
    byte data11[];
    byte[] data113;

    /*-->*/
    private Datos datos = new Datos("", "", "", "", "", "", null, null, null, null);
    private Datos d,  d1;
    private ComunicacionHttp http;
    private ComunicacionHttp1 http1;
    private Thread hiloEnvio;
    private Thread hiloEnvio1;
    private boolean seleccion = false;
    byte[] pngImage;
    private int posicion = 0;
    /*-->*/ String error = "";
    /*-->*/ double latitud = 0;
    /*-->*/ double longitud = 0;
    /*-->*/    //public Display displayMapa = null;
    TextBox txtIChannel;
//    private Image person;
    public Form form2;
    public Form form3;
    public Form form4;
    byte data1[];
    byte data[];
    public String nombreAnterior = "";
    public int bandera = 0;

    public MMSMIDlet() throws IOException {
        http = new ComunicacionHttp(this);

        http1 = new ComunicacionHttp1(this);
        d = new Datos("", "", "", "", "", "", null, null, null, null);
        d1 = new Datos("", "", "", "", "", "", null, null, null, null);
        hiloEnvio = new Thread(this);
        hiloEnvio1 = new Thread(this);
        form = new Form("Encuesta a Clientes");
        form1 = new Form("Resultado de la Encuesta");
        form2 = new Form("Busqueda de Encuesta");
        form4 = new Form("Edicion de Encuesta");
        form3 = new Form("Resultado de la Encuesta");
        txtIChannel = new TextBox("¡", null, 256, TextField.ANY);
        lista = new List("Encuestas", List.IMPLICIT);
        nombre = new TextField("Nombre:", "", 10, TextField.ANY);
        buscanombre = new TextField("Nombre a Buscar:", "", 10, TextField.ANY);
        buscanombre1 = new TextField("Nombre a Editar:", "", 10, TextField.ANY);

        exit = new Command("Salir", Command.EXIT, 1);
        atras = new Command("Atras", Command.EXIT, 1);

//        getImage = new Command("GetImage", Command.SCREEN, 1);
        camera = new Command("" +
                " Foto", Command.SCREEN, 1);
        /*-->*/ getMap = new Command("Agregar Mapa", Command.SCREEN, 1);
        guardar = new Command("Guardar", Command.OK, 1);
        crear = new Command("Crear", Command.OK, 1);
        editar = new Command("Editar", Command.OK, 1);
        transferir = new Command("Transferir", Command.OK, 1);
        eliminar = new Command("Eliminar", Command.OK, 1);
        editarencuesta = new Command("Editar Encuesta", Command.OK, 1);
        buscar = new Command("Buscar Encuesta", Command.OK, 1);
        Buscar = new Command("Buscar", Command.OK, 4);
        Buscar1 = new Command("Buscar", Command.OK, 4);
        Salir = new Command("Atras", Command.EXIT, 1);
        Camara = new Command("Foto", Command.OK, 1);
        Mapa = new Command("Agregar Mapa", Command.OK, 1);
        Guardar = new Command("Guardar", Command.OK, 1);

        cmdNO = new Command("No", Command.EXIT, 4);
        cmdSI = new Command("Si", Command.OK, 5);
        txtIChannel.addCommand(cmdNO);
        txtIChannel.addCommand(cmdSI);
        txtIChannel.setCommandListener(this);

        inicializar();
        lista.addCommand(exit);
        lista.addCommand(crear);
        lista.addCommand(editar);
        lista.addCommand(transferir);
        lista.addCommand(editarencuesta);
        lista.addCommand(buscar);
        lista.addCommand(eliminar);
        lista.setCommandListener(this);
        form.addCommand(atras);
        form.addCommand(guardar);
//        form.addCommand(storeImage);
//        form.addCommand(getImage);
        form.addCommand(camera);
        /*-->*/ form.addCommand(getMap);
        form.setCommandListener(this);
        form2.append(buscanombre);
        form2.addCommand(Salir);
        form2.addCommand(Buscar);
        form2.setCommandListener(this);
        form4.append(buscanombre1);
        form4.addCommand(Salir);
        form4.addCommand(Buscar1);
        form4.setCommandListener(this);

    }

    public void run() {
        if (bandera == 0) {
            try {
                http.enviarPorHttp(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                http1.enviarPorHttp(d1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void inicializar() throws IOException {
        form.deleteAll();
        CargarImagenes mp = new CargarImagenes(this);
        mp.start();
        nombre = new TextField("Nombre:", "", 20, TextField.ANY);
        nombre1 = new TextField("Nombre:", "", 20, TextField.ANY);
        nombre2 = new TextField("Nombre:", "", 20, TextField.UNEDITABLE);
        tel = new TextField("Telefono:", "", 20, TextField.ANY);
        dir = new TextField("Direccion:", "", 20, TextField.ANY);
        intra = new ChoiceGroup("Hace uso de la Intranet:", ChoiceGroup.EXCLUSIVE);

        intra.append("Si", null);
        intra.append("No", null);
        intranet = new ChoiceGroup("La Intranet le ha servido como una herramienta de trabajo:", ChoiceGroup.EXCLUSIVE);
        intranet.append("Nunca", null);
        intranet.append("Algunas Veces", null);
        intranet.append("Casi siempre", null);
        intranet.append("Siempre", null);
        acceso = new ChoiceGroup("El acceso a la Intranet se realiza de manera rápida y sencilla:", ChoiceGroup.EXCLUSIVE);
        acceso.append("Nunca", null);
        acceso.append("Algunas Veces", null);
        acceso.append("Casi siempre", null);
        acceso.append("Siempre", null);

        tel1 = new TextField("Telefono:", "", 20, TextField.ANY);
        dir1 = new TextField("Direccion:", "", 20, TextField.ANY);
        intra1 = new ChoiceGroup("Hace uso de la Intranet:", ChoiceGroup.EXCLUSIVE);
        tel2 = new TextField("Telefono:", "", 20, TextField.ANY);
        dir2 = new TextField("Direccion:", "", 20, TextField.ANY);
        intra2 = new ChoiceGroup("Hace uso de la Intranet:", ChoiceGroup.EXCLUSIVE);

        intra1.append("Si", null);
        intra1.append("No", null);
        intranet1 = new ChoiceGroup("La Intranet le ha servido como una herramienta de trabajo:", ChoiceGroup.EXCLUSIVE);
        intranet1.append("Nunca", null);
        intranet1.append("Algunas Veces", null);
        intranet1.append("Casi siempre", null);
        intranet1.append("Siempre", null);
        acceso1 = new ChoiceGroup("El acceso a la Intranet se realiza de manera rápida y sencilla:", ChoiceGroup.EXCLUSIVE);
        acceso1.append("Nunca", null);
        acceso1.append("Algunas Veces", null);
        acceso1.append("Casi siempre", null);
        acceso1.append("Siempre", null);

        intra2.append("Si", null);
        intra2.append("No", null);
        intranet2 = new ChoiceGroup("La Intranet le ha servido como una herramienta de trabajo:", ChoiceGroup.EXCLUSIVE);
        intranet2.append("Nunca", null);
        intranet2.append("Algunas Veces", null);
        intranet2.append("Casi siempre", null);
        intranet2.append("Siempre", null);
        acceso2 = new ChoiceGroup("El acceso a la Intranet se realiza de manera rápida y sencilla:", ChoiceGroup.EXCLUSIVE);
        acceso2.append("Nunca", null);
        acceso2.append("Algunas Veces", null);
        acceso2.append("Casi siempre", null);
        acceso2.append("Siempre", null);


        form.append(nombre);
        form.append(tel);
        form.append(dir);
        form.append(intra);
        form.append(intranet);
        form.append(acceso);

//        form1.append(nombre1);
//        form1.append(tel1);
//        form1.append(dir1);
//        form1.append(intra1);
//        form1.append(intranet1);
//        form1.append(acceso1);
        form1.addCommand(Salir);
        form1.setCommandListener(this);

        form3.addCommand(Salir);
        form3.addCommand(Camara);
        form3.addCommand(Mapa);
        form3.addCommand(Guardar);
        form3.setCommandListener(this);
    }

    public void startApp() {
        Llenar();
        Display.getDisplay(this).setCurrent(lista);
    }

    public void Camera() {

        if (resumeDisplay == null) {
            System.out.println("aqui");
            // Start the MMS connection
//      startConnection(this);
            // //Create the user interface
            cameraScreen = new CameraScreen(this);
//      infoScreen = new InfoScreen();
//      sendScreen = new SendScreen(this);
            Display.getDisplay(this).setCurrent(cameraScreen);

            resumeDisplay = cameraScreen;
            cameraScreen.start();
        } else {
            cameraScreen = new CameraScreen(this);

            Display.getDisplay(this).setCurrent(cameraScreen);

            resumeDisplay = cameraScreen;
            cameraScreen.start();
            Display.getDisplay(this).setCurrent(resumeDisplay);
//            System.out.println("aca");
        }
    }

    public void Camera1() {

        if (resumeDisplay == null) {
            System.out.println("alla camera1");
            // Start the MMS connection
//      startConnection(this);
            // //Create the user interface
            cameraScreen1 = new Camera(this);
//      infoScreen = new InfoScreen();
//      sendScreen = new SendScreen(this);
            Display.getDisplay(this).setCurrent(cameraScreen1);

            resumeDisplay = cameraScreen1;
            cameraScreen1.start();
        } else {
            cameraScreen1 = new Camera(this);

            Display.getDisplay(this).setCurrent(cameraScreen1);

            resumeDisplay = cameraScreen1;
            cameraScreen1.start();
            Display.getDisplay(this).setCurrent(resumeDisplay);
//            System.out.println("aca");
        }
    }

    public void pauseApp() {
        if (Display.getDisplay(this).getCurrent() == cameraScreen) {
            cameraScreen.stop();
        }
        if (Display.getDisplay(this).getCurrent() == cameraScreen1) {
            cameraScreen1.stop();
        }
    }

    public void destroyApp(boolean unconditional) {
        if (Display.getDisplay(this).getCurrent() == cameraScreen) {
            cameraScreen.stop();
        }
        if (Display.getDisplay(this).getCurrent() == cameraScreen1) {
            cameraScreen1.stop();
        }
    }

    void exitApplication1() {
        closeConnection();
//        Display.getDisplay(this).setCurrent(form);
//        destroyApp(false);
//        notifyDestroyed();
        Display.getDisplay(this).setCurrent(form3);
    }

    void exitApplication() {
        closeConnection();
//        Display.getDisplay(this).setCurrent(form);
//        destroyApp(false);
//        notifyDestroyed();
        Display.getDisplay(this).setCurrent(form);
    }

//  private synchronized void receive(Message incomingMessage) {
//    if (receiveScreen==null) {
//      receiveScreen = new ReceiveScreen(this);
//    }
//    receiveScreen.setMessage(incomingMessage);
//    Display.getDisplay(this).setCurrent(receiveScreen);
//  }
    public void notifyIncomingMessage(MessageConnection conn) {
        // Callback for inbound message.
        // Start a new thread to receive the message.
        new Thread() {

            public void run() {
                try {
                    Message incomingMessage = messageConnection.receive();
                    // this may be called multiple times if
                    // multiple messages arrive simultaneously
                    if (incomingMessage != null) {
//            receive(incomingMessage);
                    }
                } catch (IOException ioe) {
                    showError("Exception while receiving message: " + ioe.getMessage());
                }
            }
        }.start();
    }

    /*-->*/
    public void show(Image img, byte[] pngImage) {
        this.img = img;
        this.pngImage = pngImage;
        img = createThumbnail(img);
        ImageItem imgItem = new ImageItem("", img,
                ImageItem.LAYOUT_TOP |
                ImageItem.LAYOUT_RIGHT, null);

///*-->*/ this.mapa = map;
/*-->*/ //this.pngMap = pngMap;
/*-->*/ //img = createThumbnail(mapa);
//        /*-->*/ ImageItem imgItemMap = new ImageItem("", mapa,
//                /*-->*/ ImageItem.LAYOUT_TOP |
//                /*-->*/ ImageItem.LAYOUT_RIGHT, null);

        String n = nombre.getString();
        String d = dir.getString();
        String t = tel.getString();
        String it = null;
        if (intra.isSelected(0)) {
            it = intra.getString(0);
        } else {
            it = intra.getString(1);
        }
        String in = null;
        if (intranet.isSelected(0)) {
            in = intranet.getString(0);
        } else if (intranet.isSelected(1)) {
            in = intranet.getString(1);
        } else if (intranet.isSelected(2)) {
            in = intranet.getString(2);
        } else {
            in = intranet.getString(3);
        }
        String access = null;
        if (acceso.isSelected(0)) {
            access = acceso.getString(0);
        } else if (acceso.isSelected(1)) {
            access = acceso.getString(1);
        } else if (acceso.isSelected(2)) {
            access = acceso.getString(2);
        } else {
            access = acceso.getString(3);
        }
//        form.deleteAll();
        form.delete(0);
        form.insert(0, imgItem);
        //form.append(imgItem);

        /*-->*/ //form.insert(0,imgItemMap);
        salvar(n, d, t, it, in, access);
        Display.getDisplay(this).setCurrent(this.form);
    }

    public boolean exist(String persona) {
        boolean encontro = false;
        try {

            //RecordStore.deleteRecordStore("personas");
            RecordStore rstore = RecordStore.openRecordStore("datos", true);
            //RecordStore.deleteRecordStore("personas");
            persona = persona.toUpperCase();

            RecordEnumeration re = rstore.enumerateRecords(null, null, true);

            while (re.hasPreviousElement()) {
                byte[] b = re.previousRecord();
                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                DataInputStream dis = new DataInputStream(bais);
                String name = dis.readUTF();
                name = name.toUpperCase();
//                int pos = dis.readInt();
//                this.posicion = pos;
                if (name.equals(persona)) {
                    nombreAnterior = name;
                    encontro = true;
                    break;
                }

                bais.reset();
                dis.reset();

                bais.close();
                dis.close();

            }

            rstore.closeRecordStore();

        } catch (Exception e) {
        }
        return encontro;
    }

    public void salvar(String n, String d, String t, String it, String in, String access) {
//        nombre = new TextField("Nombre:", "", 20, TextField.ANY);
//        tel = new TextField("Telefono:", "", 20, TextField.ANY);
//        dir = new TextField("Direccion:", "", 20, TextField.ANY);
//        intra = new ChoiceGroup("Hace uso de la Intranet:", ChoiceGroup.EXCLUSIVE);
//        intra.append("Si", null);
//        intra.append("No", null);
//        intranet = new ChoiceGroup("La Intranet le ha servido como una herramienta de trabajo:", ChoiceGroup.EXCLUSIVE);
//        intranet.append("Nunca", null);
//        intranet.append("Algunas Veces", null);
//        intranet.append("Casi siempre", null);
//        intranet.append("Siempre", null);
//        acceso = new ChoiceGroup("El acceso, navegación y consulta de la Intranet se realizan de manera rápida y sencilla:", ChoiceGroup.EXCLUSIVE);
//        acceso.append("Nunca", null);
//        acceso.append("Algunas Veces", null);
//        acceso.append("Casi siempre", null);
//        acceso.append("Siempre", null);
        nombre.setString(n);
        dir.setString(d);
        tel.setString(t);
        it = it.trim();
        System.out.println(it);
        if (it.equals(intra.getString(0))) {
            intra.setSelectedIndex(0, true);
        } else {
            intra.setSelectedIndex(1, true);
        }
        if (in.equals(intranet.getString(0))) {
            intranet.setSelectedIndex(0, true);
        } else if (in.equals(intranet.getString(1))) {
            intranet.setSelectedIndex(1, true);
        } else if (in.equals(intranet.getString(2))) {
            intranet.setSelectedIndex(2, true);
        } else {
            intranet.setSelectedIndex(3, true);
        }
        if (access.equals(acceso.getString(0))) {
            acceso.setSelectedIndex(0, true);
        } else if (access.equals(acceso.getString(1))) {
            acceso.setSelectedIndex(1, true);
        } else if (access.equals(acceso.getString(2))) {
            acceso.setSelectedIndex(2, true);
        } else {
            acceso.setSelectedIndex(3, true);
        }
//        form.append(nombre);
//        form.append(tel);
//        form.append(dir);
//        form.append(intra);
//        form.append(intranet);
//        form.append(acceso);
    }

//Create a method for fetching the application ID.

    // return the application id, either from the
    // jad file or from a hardcoded value
    String getApplicationID() {
        String applicationID = this.getAppProperty("Application-ID");
        return applicationID == null ? APPLICATION_ID : applicationID;
    }

    //Create the methods for showing the various screens and displays used by the application.
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    // Upon capturing an image, show the compose screen
    void imageCaptured(byte[] imageData) {
        cameraScreen.stop();
    }

    void imageCaptured1(byte[] imageData) {
        cameraScreen1.stop();
    }

    // Displays the error screen
    void showError(String messageString) {
        Alert alerta = new Alert(messageString);
        Display.getDisplay(this).setCurrent(alerta);
//    infoScreen.showError(messageString, Display.getDisplay(this) );
    }

//Create methods for starting and closing the message connection. The setMessageListener method registers a MessageListener object that the platform can notify when a message has been received on this MessageConnection.
//For more information, see setMessageListener in the WMAPI 2.0 specification.
    // Closes the message connection when the application
    // is stopped
    private void closeConnection() {
        closing = true;
        if (messageConnection != null) {
            try {
                messageConnection.close();
            } catch (IOException ioe) {
                // Ignore errors on shutdown
            }
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exit) {
            destroyApp(true);
            notifyDestroyed();
        } else if (c == buscar) {
            Display.getDisplay(this).setCurrent(form2);
        } else if (c == Salir) {
            Display.getDisplay(this).setCurrent(lista);
        } else if (c == editarencuesta) {
            Display.getDisplay(this).setCurrent(form4);

        } else if (c == Mapa) {
            MapService mp = new MapService(this);
            mp.start();
        } else if (c == Buscar) {
            Lookup mp = new Lookup(this);
            mp.start();

        } else if (c == Buscar1) {
            Editar mp = new Editar(this);
            mp.start();
        } else if (c == cmdSI) {

            boolean flag = true;
            try {
                long numero = Integer.parseInt(tel.getString());
            } catch (Exception e) {
                Alert al = new Alert("Mensaje de Error", "El numero telefonico no es valido.", null, AlertType.ERROR);
                //al.setTimeout(Alert.FOREVER);
                Display.getDisplay(this).setCurrent(al);
                flag = false;

            }
            String it = null;
            if (intra.isSelected(0)) {
                it = intra.getString(0);
            } else {
                it = intra.getString(1);
            }
            String in = null;
            if (intranet.isSelected(0)) {
                in = intranet.getString(0);
            } else if (intranet.isSelected(1)) {
                in = intranet.getString(1);
            } else if (intranet.isSelected(2)) {
                in = intranet.getString(2);
            } else {
                in = intranet.getString(3);
            }
            String access = null;
            if (acceso.isSelected(0)) {
                access = acceso.getString(0);
            } else if (acceso.isSelected(1)) {
                access = acceso.getString(1);
            } else if (acceso.isSelected(2)) {
                access = acceso.getString(2);
            } else {
                access = acceso.getString(3);
            }

            nombre.setString(nombreAnterior);
            datos.setNombre(nombre.getString());
            datos.setTel(tel.getString());
            datos.setDir(dir.getString());
            datos.setIntra(intra.getString(intra.getSelectedIndex()));
            datos.setIntranet(intranet.getString(intranet.getSelectedIndex()));
            datos.setAcceso(acceso.getString(acceso.getSelectedIndex()));
            datos.setImagen(img);
            datos.setPngImage(pngImage);
            datos.setMapa(mapa);
            datos.setPngMap(pngMap);
            if (flag == true) {
                try {
                    int pos = lista.getSelectedIndex();
                    String person = lista.getString(pos);
                    //lista.delete(pos);
                    System.out.println(pos);
                    Encuesta.modificarEncuesta(datos, person);
                    posicion++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Llenar();
                Display.getDisplay(this).setCurrent(lista);
            } else {
//                Display.getDisplay(this).setCurrent(form);
//                Alert al = new Alert("El campo numero telefinico es incorrecto");
//                Display.getDisplay(this).setCurrent(al);
            }

        } else if (c == cmdNO) {
            Display.getDisplay(this).setCurrent(form);
        } else if (c == crear) {
            try {
                inicializar();
                seleccion = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Display.getDisplay(this).setCurrent(form);
        } else if (c == atras) {
            Display.getDisplay(this).setCurrent(lista);
        } else if (c == Guardar) {
            Transferir1();
        } else if (c == guardar) {
            boolean flag = true;
            try {
                long numero = Integer.parseInt(tel.getString());
            } catch (Exception e) {
                Alert al = new Alert("Mensaje de Error", "El numero telefonico no es valido.", null, AlertType.ERROR);
                Display.getDisplay(this).setCurrent(al);
                flag = false;
            }
            if (exist(nombre.getString()) == true && seleccion == false) {
                //txtIChannel = new TextBox("¡", "Este nombre de contacto ya existe, ¿Reemplazar? "+nombre.getString(), 256, TextField.ANY);
                txtIChannel.setString("Este nombre de contacto ya existe, ¿Reemplazar? " + nombre.getString());
                Display.getDisplay(this).setCurrent(txtIChannel);
            } else {
                String it = null;
                if (intra.isSelected(0)) {
                    it = intra.getString(0);
                } else {
                    it = intra.getString(1);
                }
                String in = null;
                if (intranet.isSelected(0)) {
                    in = intranet.getString(0);
                } else if (intranet.isSelected(1)) {
                    in = intranet.getString(1);
                } else if (intranet.isSelected(2)) {
                    in = intranet.getString(2);
                } else {
                    in = intranet.getString(3);
                }
                String access = null;
                if (acceso.isSelected(0)) {
                    access = acceso.getString(0);
                } else if (acceso.isSelected(1)) {
                    access = acceso.getString(1);
                } else if (acceso.isSelected(2)) {
                    access = acceso.getString(2);
                } else {
                    access = acceso.getString(3);
                }


                datos.setNombre(nombre.getString());
                datos.setTel(tel.getString());
                datos.setDir(dir.getString());
                datos.setIntra(intra.getString(intra.getSelectedIndex()));
                datos.setIntranet(intranet.getString(intranet.getSelectedIndex()));
                datos.setAcceso(acceso.getString(acceso.getSelectedIndex()));
                datos.setImagen(img);
                datos.setPngImage(pngImage);
                datos.setMapa(mapa);
                datos.setPngMap(pngMap);
                System.out.println(seleccion);
                if (seleccion == false && flag == true) {
                    Encuesta.almacenaEncuesta(datos);
                    Llenar();
                    Display.getDisplay(this).setCurrent(lista);
                    posicion++;
                } else if (seleccion == true && flag == true) {
                    int pos = lista.getSelectedIndex();
                    String person = lista.getString(pos);
                    //lista.delete(pos);
                    System.out.println(pos);
                    Encuesta.modificarEncuesta(datos, person);
                    Llenar();
                    Display.getDisplay(this).setCurrent(lista);
                    posicion++;
                }


            }
        } else if (c == editar) {
            if (lista.size() != 0) {
                form.deleteAll();
                leerResultados();
                seleccion = true;
                Display.getDisplay(this).setCurrent(form);
            } else {

                Alert alerta = new Alert("Mensaje de Error", "No hay algun objeto seleccionado.", null, AlertType.ERROR);
                Display.getDisplay(this).setCurrent(alerta);
            }
        } else if (c == camera) {
            Camera();
        } else if (c == Camara) {
            Camera1();
        } else if (c == transferir) {
            if (lista.size() != 0) {
                Transferir();
            } else {

                Alert alerta = new Alert("Mensaje de Error", "No hay algun objeto seleccionado.", null, AlertType.ERROR);
                Display.getDisplay(this).setCurrent(alerta);
            }
        } else if (c == eliminar) {
            if (lista.size() != 0) {

                try {

                    int k = lista.getSelectedIndex();
                    String nomb = lista.getString(k);
                    nomb = nomb.trim();
                    lista.delete(k);
                    Alert alerta = new Alert("Eliminando....");
                    Display.getDisplay(this).setCurrent(alerta);
                    Encuesta.delete(nomb);

                } catch (Exception ex) {

                    ex.printStackTrace();
                }


            } else {
                Alert alerta = new Alert("Mensaje de Error", "No hay algun objeto seleccionado.", null, AlertType.ERROR);
                Display.getDisplay(this).setCurrent(alerta);
            }
        /*-->*/ } else if (c == getMap) {
            /*-->*/ MapServices mp = new MapServices(this);
            mp.start();
        /*-->*/ }
    }

    public void Transferir() {
        try {
            bandera = 0;
            String p = lista.getString(lista.getSelectedIndex());


//            System.out.println(p);
            Vector v = Encuesta.leerEncuesta();
            datos = (Datos) v.elementAt(lista.getSelectedIndex());
            String nomb = datos.getNombre();
            System.out.println("Nombre" + nomb);
            String tele = datos.getTel();
            System.out.println("Television" + tele);


            String direccion = datos.getDir();
            System.out.println("Television" + direccion);
            String it = datos.getIntra();
            System.out.println("Intranet" + it);
            String in = datos.getIntranet();
            System.out.println("Intranet" + in);
            String access = datos.getAcceso();
            System.out.println("Intranet" + access);
            img = datos.getImagen();
            img = createThumbnail(img);
            ImageItem imgItem = new ImageItem("", img,
                    ImageItem.LAYOUT_TOP |
                    ImageItem.LAYOUT_RIGHT, null);
            System.out.println("Image" + imgItem);
            mapa = datos.getMapa();
            d.setNombre(nomb);
            d.setTel(tele);
            d.setDir(direccion);
            d.setIntra(it);
            d.setIntranet(in);
            d.setAcceso(access);
            d.setImagen(img);
            d.setPngImage(datos.getPngImage());
            d.setMapa(mapa);
            d.setPngMap(datos.getPngMap());
            byte[] pngImage = datos.getPngImage();
            System.out.println("Transferir:prim:ult:" + pngImage[0] + " " + pngImage[pngImage.length - 1]);
            byte[] pngMap = datos.getPngMap();

            hiloEnvio = new Thread(this);
            hiloEnvio.start();


        /* textBoxResultados.insert(nom, 0);
        textBoxResultados.insert(gen, textBoxResultados.size());
        textBoxResultados.insert(afs, textBoxResultados.size());

        display.setCurrent(textBoxResultados);*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void Transferir1() {
        try {

            bandera = 1;

            nombre11=nombre2.getString();
            System.out.println(nombre11);
            tel11 = tel2.getString();
            System.out.println(tel11);
            dir11 = dir2.getString();
            if (intra2.isSelected(0)) {
                intr11 = intra2.getString(0);
            } else {
                intr11 = intra2.getString(1);
            }

            if (intranet2.isSelected(0)) {
                intra11 = intranet2.getString(0);
            } else if (intranet2.isSelected(1)) {
                intra11 = intranet2.getString(1);
            } else if (intranet2.isSelected(2)) {
                intra11 = intranet.getString(2);
            } else {
                intra11 = intranet.getString(3);
            }

            if (acceso2.isSelected(0)) {
                acces11 = acceso2.getString(0);
            } else if (acceso2.isSelected(1)) {
                acces11 = acceso2.getString(1);
            } else if (acceso2.isSelected(2)) {
                acces11 = acceso2.getString(2);
            } else {
                acces11 = acceso2.getString(3);
            }
            d1.setNombre(nombre11);
            d1.setTel(tel11);
            d1.setDir(dir11);
            d1.setIntra(intr11);
            d1.setIntranet(intra11);
            d1.setAcceso(acces11);
            d1.setPngImage(data113);
            d1.setPngMap(data11);
            byte[] pngImage = data113;
            System.out.println("Transferir:prim:ult:" + pngImage[0] + " " + pngImage[pngImage.length - 1]);
            byte[] pngMap = data11;

            hiloEnvio1 = new Thread(this);
            hiloEnvio1.start();


        /* textBoxResultados.insert(nom, 0);
        textBoxResultados.insert(gen, textBoxResultados.size());
        textBoxResultados.insert(afs, textBoxResultados.size());

        display.setCurrent(textBoxResultados);*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void traerDatos(String nomb, int telef, String dire) throws IOException {
        inicializar();

        nombre.setString(nomb);
        tel.setString(String.valueOf(tel));
        dir.setString(dire);
        Display.getDisplay(this).setCurrent(form);

    }

    public void leerResultados() {
        try {

            String p = lista.getString(lista.getSelectedIndex());
            System.out.println(p);
            Vector v = Encuesta.leerEncuesta();
            datos = (Datos) v.elementAt(lista.getSelectedIndex());
            String nomb = datos.getNombre();
            String tele = datos.getTel();
            String direcc = datos.getDir();
            String it = datos.getIntra();
            it = it.trim();
            String in = datos.getIntranet();

            String access = datos.getAcceso();
            Image imag = datos.getImagen();
            imag = createThumbnail(imag);
            ImageItem imgItem = new ImageItem("", imag,
                    ImageItem.LAYOUT_TOP |
                    ImageItem.LAYOUT_RIGHT, null);
            form.insert(0, imgItem);
//            pngImage = datos.getPngImage();
            Image map = datos.getMapa();
//            map=createThumbnail(map);
            ImageItem imgItem1 = new ImageItem("", map,
                    ImageItem.LAYOUT_TOP |
                    ImageItem.LAYOUT_RIGHT, null);
            form.insert(1, imgItem1);
//            pngMap = datos.getPngMap();
            nombre = new TextField("Nombre:", "", 20, TextField.ANY);
            tel = new TextField("Telefono:", "", 20, TextField.ANY);
            dir = new TextField("Direccion:", "", 20, TextField.ANY);
            intra = new ChoiceGroup("Hace uso de la Intranet:", ChoiceGroup.EXCLUSIVE);
            intra.append("Si", null);
            intra.append("No", null);
            intranet = new ChoiceGroup("La Intranet le ha servido como una herramienta de trabajo:", ChoiceGroup.EXCLUSIVE);
            intranet.append("Nunca", null);
            intranet.append("Algunas Veces", null);
            intranet.append("Casi siempre", null);
            intranet.append("Siempre", null);
            acceso = new ChoiceGroup("El acceso a la Intranet se realiza de manera rápida y sencilla:", ChoiceGroup.EXCLUSIVE);
            acceso.append("Nunca", null);
            acceso.append("Algunas Veces", null);
            acceso.append("Casi siempre", null);
            acceso.append("Siempre", null);
            form.append(nombre);
            form.append(tel);
            form.append(dir);
            form.append(intra);
            form.append(intranet);
            form.append(acceso);
            nombre.setString(nomb);
            tel.setString(tele);
            dir.setString(direcc);
            if (it.equals(intra.getString(0))) {
                intra.setSelectedIndex(0, true);
            } else {
                intra.setSelectedIndex(1, true);
            }
            if (in.equals(intranet.getString(0))) {
                intranet.setSelectedIndex(0, true);
            } else if (in.equals(intranet.getString(1))) {
                intranet.setSelectedIndex(1, true);
            } else if (in.equals(intranet.getString(2))) {
                intranet.setSelectedIndex(2, true);
            } else {
                intranet.setSelectedIndex(3, true);
            }
            if (access.equals(acceso.getString(0))) {
                acceso.setSelectedIndex(0, true);
            } else if (access.equals(acceso.getString(1))) {
                acceso.setSelectedIndex(1, true);
            } else if (access.equals(acceso.getString(2))) {
                acceso.setSelectedIndex(2, true);
            } else {
                acceso.setSelectedIndex(3, true);
            }

        /* textBoxResultados.insert(nom, 0);
        textBoxResultados.insert(gen, textBoxResultados.size());
        textBoxResultados.insert(afs, textBoxResultados.size());

        display.setCurrent(textBoxResultados);*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void agregar1(String nombre12, int tel12, String dir12, String intr12, String intra12, String acces12, ImageItem imageItem12, ImageItem imageItem123,
            byte data12[], byte[] data13) {
//        System.out.println(data1.length);
//        System.out.println(data.length);
//        this.data1 = data1;
//        this.data = data;
        form1.deleteAll();

        form1.insert(0, imageItem123);
        form1.insert(1, imageItem12);
        nombre1.setString(nombre12);
        tel1.setString(String.valueOf(tel12));
        dir1.setString(dir12);
        if (intr12.equals(intra1.getString(0))) {
            intra1.setSelectedIndex(0, true);
        } else {
            intra1.setSelectedIndex(1, true);
        }
        if (intra12.equals(intranet1.getString(0))) {
            intranet1.setSelectedIndex(0, true);
        } else if (intra12.equals(intranet1.getString(1))) {
            intranet1.setSelectedIndex(1, true);
        } else if (intra12.equals(intranet1.getString(2))) {
            intranet1.setSelectedIndex(2, true);
        } else {
            intranet1.setSelectedIndex(3, true);
        }
        if (acces12.equals(acceso1.getString(0))) {
            acceso1.setSelectedIndex(0, true);
        } else if (acces12.equals(acceso1.getString(1))) {
            acceso1.setSelectedIndex(1, true);
        } else if (acces12.equals(acceso1.getString(2))) {
            acceso1.setSelectedIndex(2, true);
        } else {
            acceso1.setSelectedIndex(3, true);
        }

        form1.append(nombre1);
        form1.append(tel1);
        form1.append(dir1);
        form1.append(intra1);
        form1.append(intranet1);
        form1.append(acceso1);
        Display.getDisplay(this).setCurrent(form1);

    }

    public void agregar(String nombre, int tel, String dir, String intr, String intra, String acces, ImageItem imageItem, ImageItem imageItem1,
            byte data1[], byte[] data) {


//        this.nombre11 = nombre;
//        this.tel11 = String.valueOf(tel);
//        this.dir11 = dir;
//        this.intr11 = intr;
//        this.intra11 = intra;
//        this.acces11 = acces;
        this.data11 = data1;
        this.data113 = data;
        System.out.println(data1.length);
        System.out.println(data.length);
//        this.data1 = data1;
//        this.data = data;
        form3.deleteAll();

        form3.insert(0, imageItem1);
        form3.insert(1, imageItem);

        nombre2.setString(nombre);
        tel2.setString(String.valueOf(tel));
        dir2.setString(dir);
        if (intr.equals(intra2.getString(0))) {
            intra2.setSelectedIndex(0, true);
        } else {
            intra2.setSelectedIndex(1, true);
        }
        if (intra.equals(intranet2.getString(0))) {
            intranet2.setSelectedIndex(0, true);
        } else if (intra.equals(intranet2.getString(1))) {
            intranet2.setSelectedIndex(1, true);
        } else if (intra.equals(intranet2.getString(2))) {
            intranet2.setSelectedIndex(2, true);
        } else {
            intranet2.setSelectedIndex(3, true);
        }
        if (acces.equals(acceso2.getString(0))) {
            acceso2.setSelectedIndex(0, true);
        } else if (acces.equals(acceso2.getString(1))) {
            acceso2.setSelectedIndex(1, true);
        } else if (acces.equals(acceso2.getString(2))) {
            acceso2.setSelectedIndex(2, true);
        } else {
            acceso2.setSelectedIndex(3, true);
        }

//        for(int i=0;i<data1.length;i++){
//            System.out.println( data1[i]);
//        }
//
//        System.out.println("---------------------------------------------");
//         for(int i=0;i<data.length;i++){
//            System.out.println( data[i]);
//        }
        form3.append(nombre2);
        form3.append(tel2);
        form3.append(dir2);
        form3.append(intra2);
        form3.append(intranet2);
        form3.append(acceso2);
        Display.getDisplay(this).setCurrent(form3);

    }

    public void Llenar() {
        lista.deleteAll();
        try {
            dato = Encuesta.leerEncuesta();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dato.isEmpty()) {
            Alert alerta = new Alert("No hay datos");
            Display.getDisplay(this).setCurrent(alerta);
        } else {
            System.out.println("tamaño" + dato.size());
            for (int i = 0; i < dato.size(); i++) {
                datos = (Datos) dato.elementAt(i);
                System.out.println("NOMBRE " + i);
                lista.append(datos.getNombre(), createThumbnail1(datos.getImagen()));
            }
        }
    }

    private Image createThumbnail(Image image) {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();

        int thumbWidth = 30;//64
        int thumbHeight = -1;//

        if (thumbHeight == -1) {
            thumbHeight = thumbWidth * sourceHeight / sourceWidth;
        }

        Image thumb = Image.createImage(thumbWidth, thumbHeight);
        Graphics g = thumb.getGraphics();

        for (int y = 0; y < thumbHeight; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                g.setClip(x, y, 1, 1);
                int dx = x * sourceWidth / thumbWidth;
                int dy = y * sourceHeight / thumbHeight;
                g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
            }
        }

        Image immutableThumb = Image.createImage(thumb);

        return immutableThumb;
    }

    private Image createThumbnail1(Image image) {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();

        int thumbWidth = 40;//64
        int thumbHeight = -1;//

        if (thumbHeight == -1) {
            thumbHeight = thumbWidth * sourceHeight / sourceWidth;
        }

        Image thumb = Image.createImage(thumbWidth, thumbHeight);
        Graphics g = thumb.getGraphics();

        for (int y = 0; y < thumbHeight; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                g.setClip(x, y, 1, 1);
                int dx = x * sourceWidth / thumbWidth;
                int dy = y * sourceHeight / thumbHeight;
                g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
            }
        }

        Image immutableThumb = Image.createImage(thumb);

        return immutableThumb;
    }

    /*-->*/
    private void obtenerMapa() {
        MapServices mp = new MapServices(this);
        mp.run();
    }
    /*-->*/
}