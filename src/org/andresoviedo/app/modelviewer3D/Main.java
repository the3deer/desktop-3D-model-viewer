package org.andresoviedo.app.modelviewer3D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.andresoviedo.app.modelviewer3D.cameras.Camera;
import org.andresoviedo.app.modelviewer3D.entities.Transformation;
import org.andresoviedo.app.modelviewer3D.gui.JPanelObjects;
import org.andresoviedo.app.modelviewer3D.gui.Object3DLoadDialog;
import org.andresoviedo.app.modelviewer3D.gui.SceneControl;
import org.andresoviedo.app.modelviewer3D.loaders.obj.OBJLoaderGL.OBJModel;
import org.andresoviedo.app.modelviewer3D.loaders.obj.tegr.Tegr3dObjectLoader;
import org.andresoviedo.app.modelviewer3D.objects.Light;
import org.andresoviedo.app.modelviewer3D.objects.Object3D;
import org.andresoviedo.app.modelviewer3D.objects.WaterDrop;
import org.andresoviedo.app.modelviewer3D.objects.room.Axis;
import org.andresoviedo.app.modelviewer3D.objects.room.EmbededObject3D;
import org.andresoviedo.app.modelviewer3D.objects.room.LightBulb;
import org.andresoviedo.util.swing.ScreenResSelector;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.GLUT;

public class Main extends JFrame implements WindowListener,
		GLEventListener, MouseListener, MouseMotionListener, KeyListener,
		ActionListener, MouseWheelListener {

	private static final float PROJECTION_NEAR = 1f;

	private static final float PROJECTION_FAR = 1000f;

	private static final long serialVersionUID = -7791974509561315284L;

	private static Main instance;

	private JPanelObjects objectsPanel;

	private SceneControl sceneControlPanel;

	private List<Object3D> objs = new Vector<Object3D>();

	private List<Object3D> newObjs = new Vector<Object3D>();

	private List<Object3D> oldObjs = new Vector<Object3D>();

	// private List<Integer> objsCLs = new Vector<Integer>();

	// private long startTime;
	// private int frameCount;
	// private float fps;
	// private static Font fpsFont = new Font("SansSerif", Font.BOLD, 24);
	// private DecimalFormat format = new DecimalFormat("####.00");

	// Screen variables
	GraphicsDevice dev;

	DisplayMode origMode;

	boolean fullScreen;

	int initWidth = 640;

	int initHeight = 480;

	// OpenGL canvas
	private GLCanvas canvas;

	// // lightning parameters
	// float[] light_position = new float[]{2.5F, 2.5F, 2.5F, 1.0F};
	// // float[] spot_direction = new float[]{-light_position[0],
	// // -light_position[1], -light_position[2], 1.0F};
	// float[] spot_direction = new float[]{0, 0, 0, 1.0F};
	// float[] light_ambient = new float[]{0.1F, 0.1F, 0.1F, 1.0F};
	// float[] light_diffuse = new float[]{1.0F, 1.0F, 1.0F, 1.0F};
	// float[] light_specular = new float[]{1.0F, 1.0F, 1.0F, 1.0F};

	public List<Light> lights = new Vector<Light>();
	{
		Light light0 = new Light(GL.GL_LIGHT0);
		light0.enabled = true;
		light0.light_position = new float[] { 0F, 100F, 100F, 1.0F };
		lights.add(light0);
		lights.add(new Light(GL.GL_LIGHT1));
		lights.add(new Light(GL.GL_LIGHT2));
		lights.add(new Light(GL.GL_LIGHT3));
		lights.add(new Light(GL.GL_LIGHT4));
		lights.add(new Light(GL.GL_LIGHT5));
		lights.add(new Light(GL.GL_LIGHT6));
		lights.add(new Light(GL.GL_LIGHT7));
	}

	LightBulb lightBulb = new LightBulb();

	int clLight = -1;

	// private List<Integer> clLights = new Vector<Integer>();
	// {
	// for (int i = 0; i < lights.size(); i++) {
	// clLights.add(-1);
	// }
	// }

	// Camera implementation
	Camera camera = new Camera();

	// Working variables
	Animator animator;

	private final static float[] WORLD_CENTER = new float[] { 0, 0, 0 };

	float[] cameraPos = new float[] { 0, 0, 2 };

	boolean cameraChanged = false;

	boolean redrawOnlyModified = false;

	public boolean forceRedrawAll = false;

	public boolean enableLighting = false;

	public boolean enableTextures = false;

	public boolean enableDrawSolid = false;

	public boolean drawNormals = false;

	public boolean drawBoundingBox = false;

	public boolean drawSmooth = false;

	public boolean hintAntiAliasing = true;

	public boolean enablePerspective = true;

	public boolean selectObject = false;

	// Axis object
	Axis axis = new Axis();

	int clAxis = -1;

	// Scene transformation
	Transformation mouseTransformation = new Transformation();

	Transformation sceneTransformation = new Transformation();

	boolean redrawScene = false;

	// Object selection
	int objectSelected = -1;

	float[] objectSelectedColor = new float[] { 0F, 0F, 1F };

	// List<Object3D> unselectableObjs = new Vector<Object3D>();
	Object3D unselectableObjs = null;

	Object3D uncollisionableObj = null;

	Timer timer = new Timer();

	TimerTask rainTask = new RainTask();

	List<WaterDrop> waterDrops = new Vector<WaterDrop>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}

	public static Main getInstance() {
		return instance;
	}

	public Main() {
		super("3D Model Tool");
		instance = this;
		init();
	}

	private void init() {
		setMenuBar(createMenuBar());
		GLCanvas glCanvas = createGLCanvas();
		getContentPane().add(glCanvas, BorderLayout.CENTER);

		Box controlBox = Box.createVerticalBox();
		objectsPanel = createObjectsList();
		objectsPanel.setPreferredSize(new Dimension(150, 150));
		sceneControlPanel = new SceneControl();
		sceneControlPanel.setPreferredSize(new Dimension(150, 150));
		controlBox.add(objectsPanel);
		controlBox.add(sceneControlPanel);
		controlBox.add(Box.createVerticalGlue());
		getContentPane().add(controlBox, BorderLayout.EAST);

		setBackground(Color.WHITE);
		setSize(initWidth, initHeight);
		setLocation(getCentralPoint());

		addMouseWheelListener(this);
		addWindowListener(this);

		loadObjectsAndCLs();

		objectsPanel.reload();
		sceneControlPanel.update();

		setVisible(true);

		// Translate focus on main panel
		glCanvas.requestFocus();
	}

	private GLCanvas createGLCanvas() {
		canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);

		// addMouseListener(this);
		// addMouseMotionListener(this);
		// addKeyListener(this);
		camera.SetCamera(cameraPos[0], cameraPos[1], cameraPos[2],
				WORLD_CENTER[0], WORLD_CENTER[1], WORLD_CENTER[2], 0.0F, 1.0F,
				0.0F);
		animator = new Animator(canvas);
		animator.start();
		return canvas;
	}

	private MenuBar createMenuBar() {
		MenuBar mbarra = new MenuBar();
		Menu m = new Menu("Archivo");
		MenuItem menuLoadObject = new MenuItem("Load object...");
		menuLoadObject.setActionCommand("loadObject");
		menuLoadObject.addActionListener(this);
		m.add(menuLoadObject);

		// menuLoadObject = new MenuItem("Change display mode...");
		// menuLoadObject.setActionCommand("changeDisplay");
		// menuLoadObject.addActionListener(this);
		// m.add(menuLoadObject);
		// m.add(new MenuItem("Abrir"));
		// m.add(new MenuItem("Guardar"));
		// m.add(new MenuItem("Guardar como"));
		// m.add(new MenuItem("Imprimir"));
		// m.addSeparator();
		MenuItem menuExit = new MenuItem("Exit");
		menuExit.setActionCommand("exit");
		menuExit.addActionListener(this);
		m.add(menuExit);
		mbarra.add(m);

		// m = new Menu("Ayuda");
		// m.add(new MenuItem("Ayuda!"));
		// m.addSeparator();
		// m.add(new MenuItem("Acerca de..."));
		// mbarra.add(m);

		return mbarra;
	}

	private JPanelObjects createObjectsList() {
		JPanelObjects jpo = new JPanelObjects();
		return jpo;
	}

	private void loadObjectsAndCLs() {
		// objs.add(new FloorObject());
		// objs.add(new WallsObject());
		// objs.add(new CeilingObject());
		// objs.add(new LightBulb());
		objs.add(axis);

		try {
			Tegr3dObjectLoader obj = new Tegr3dObjectLoader(
					".\\models\\0_esfera2.3d");
			obj.textureFile = new File(
					".\\textures\\26051178.Friscosnowpanoramic.jpg");
			obj.load();
			obj.reshape(WORLD_CENTER, 20, 20, 20);
			obj.rotY = 180;
			objs.add(obj);
			unselectableObjs = obj;
			uncollisionableObj = obj;

			obj = new Tegr3dObjectLoader(".\\models\\cube_without_normals.3d");
			obj.load();
			obj.reshape(WORLD_CENTER, 1, 1, 1);
			// obj.ani_rotation[1] = -1;
			// obj.ani_enabled = true;
			obj.transY += 4F;
			objs.add(obj);

			obj = new Tegr3dObjectLoader(".\\models\\0_beethoven.3d");
			obj.invertedNormals = true;
			obj.load();
			obj.reshape(WORLD_CENTER, 1, 1, 1);
			obj.transY += 7F;
			// obj.ani_rotation[1] = 5;
			// obj.ani_enabled = true;
			objs.add(obj);

			obj = new Tegr3dObjectLoader(".\\models\\0_home.3d");
			obj.invertedNormals = true;
			obj.load();
			obj.reshape(WORLD_CENTER, 1, 1, 1);
			// obj.transY += 0.5F;
			// obj.ani_rotation[1] = 5;
			// obj.ani_enabled = true;
			objs.add(obj);

			obj = new Tegr3dObjectLoader(".\\models\\0_esfera2.3d");
			obj.textureFile = new File(".\\models\\EarthTM0361.jpg");
			obj.load();
			obj.reshape(WORLD_CENTER, 1, 1, 1);
			obj.transX += 2;
			obj.ani_rotation[1] = 4;
			obj.ani_translation[1] = 0.05F;
			obj.ani_enabled = true;
			objs.add(obj);

			obj = new Tegr3dObjectLoader(".\\models\\0_esfera2.3d");
			obj.textureFile = new File(".\\textures\\MarsV3-Shaded-2k.jpg");
			obj.load();
			obj.reshape(WORLD_CENTER, 1, 1, 1);
			obj.transX += 4;
			obj.ani_rotation[1] = 2;
			obj.ani_translation[1] = 0.2F;
			obj.ani_enabled = true;
			objs.add(obj);

			obj = new Tegr3dObjectLoader(".\\models\\0_esfera2.3d");
			obj.textureFile = new File(".\\textures\\neptune.jpg");
			obj.load();
			obj.reshape(WORLD_CENTER, 1, 1, 1);
			obj.transX += 6;
			obj.ani_rotation[1] = 1;
			obj.ani_translation[1] = 0.5F;
			obj.ani_enabled = true;
			objs.add(obj);

			//
			// obj = new Tegr3dObjectLoader("\\models\\cube_points.3d");
			// obj.transX = 10F;
			// obj.transY = 10F;
			// obj.transZ = 300F;
			// obj.scaleX = 100;
			// obj.scaleY = 100;
			// obj.scaleZ = 100;
			// objs.add(obj);

			// obj = new Tegr3dObjectLoader("\\models\\0_beethoven.3d");
			// obj.offset = 0;
			// obj.transX = 100F;
			// obj.transY = 100F;
			// obj.transZ = 600F;
			// obj.scaleX = 10;
			// obj.scaleY = 10;
			// obj.scaleZ = 10;
			// objs.add(obj);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Failed to load tegr model: "
					+ ex.getMessage());
		}

		// for (int i = 0; i < objs.size(); i++) {
		// objsCLs.add(-1);
		// }

		forceRedrawAll = true;
	}

	public List<Object3D> getObjects3D() {
		return objs;
	}

	public Camera getCamera() {
		return camera;
	}

	private Point getCentralPoint() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - this.getWidth()) / 2;
		int y = (screenSize.height - this.getHeight()) / 2;
		return new Point(x, y);
	}

	private void showChangeDisplayModeDialog() {
		// Show screen size dialog
		dev = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		origMode = dev.getDisplayMode();
		DisplayMode newMode = null;
		if (dev.isFullScreenSupported()) {
			newMode = ScreenResSelector.showSelectionDialog();
			if (newMode != null) {
				initWidth = newMode.getWidth();
				initHeight = newMode.getHeight();
				setUndecorated(true);
			}
		}
		// Enter full screen and change mode if supported
		if (dev.isFullScreenSupported() && (newMode != null)) {
			dev.setFullScreenWindow(this);
			if (dev.isDisplayChangeSupported()) {
				dev.setDisplayMode(newMode);
				fullScreen = true;
			} else {
				// Not much point in having a full-screen window in this case
				dev.setFullScreenWindow(null);
				final Frame f2 = this;
				try {
					EventQueue.invokeAndWait(new Runnable() {
						public void run() {
							f2.setVisible(false);
							f2.setUndecorated(false);
							f2.setVisible(true);
							f2.setSize(initWidth, initHeight);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.err
						.println("NOTE: was not able to change display mode; full-screen disabled");
			}
		}
	}

	// public void paint(Graphics g) {
	// super.paint(g);
	// if (startTime == 0) {
	// startTime = System.currentTimeMillis();
	// }
	//
	// if (++frameCount == 30) {
	// long endTime = System.currentTimeMillis();
	// fps = 30.0f / (float) (endTime - startTime) * 1000;
	// frameCount = 0;
	// startTime = System.currentTimeMillis();
	// }
	//
	// if (fps > 0) {
	// g.setColor(Color.BLACK);
	// g.setFont(fpsFont);
	// g.drawString("FPS: " + format.format(fps), getWidth() - 140,
	// getHeight() - 30);
	// }
	//
	// int sp = 10;
	// // if (javaImage != null) {
	// // g.drawImage(javaImage, sp,
	// // getHeight() - javaImage.getHeight() - sp, null);
	// // if (openglImage != null) {
	// // g.drawImage(openglImage, sp + javaImage.getWidth() + sp,
	// // getHeight() - openglImage.getHeight() - sp, null);
	// // }
	// // }
	// }

	public void init(GLAutoDrawable drawable) {

		GL gl = drawable.getGL();

		axis.glList = axis.createCallList(gl, enableDrawSolid, enableLighting,
				enableTextures);
		if (drawNormals && clAxis == -1) {
			clAxis = axis.glList;
		}

		// Lights
		if (enableLighting) {
			if (clLight == -1) {
				clLight = lightBulb.createCallList(gl);
			}
			for (Light light : lights) {
				if (light.enabled) {
					gl.glLightfv(light.id, GL.GL_AMBIENT, light.light_ambient,
							0);
					gl.glLightfv(light.id, GL.GL_DIFFUSE, light.light_diffuse,
							0);
					gl.glLightfv(light.id, GL.GL_SPECULAR,
							light.light_specular, 0);
					gl.glLightfv(light.id, GL.GL_POSITION,
							light.light_position, 0);
					gl.glLightfv(light.id, GL.GL_SPOT_DIRECTION,
							light.spot_direction, 0);
					gl.glEnable(light.id);
				} else {
					gl.glDisable(light.id);
				}
			}
			gl.glEnable(GL.GL_LIGHTING);
			gl.glEnable(GL.GL_NORMALIZE);
			// Optimizations
			if (drawSmooth) {
				gl.glShadeModel(GL.GL_SMOOTH);
			} else {
				gl.glShadeModel(GL.GL_FLAT);
			}
		}
		gl.glEnable(GL.GL_DEPTH_TEST);

		if (hintAntiAliasing) {
			gl.glHint(GL.GL_POINT_SMOOTH_HINT, GL.GL_DONT_CARE);
			gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);
			gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_DONT_CARE);
		}

		synchronized (objs) {
			for (int i = 0; i < objs.size(); i++) {
				Object3D obj = objs.get(i);
				if (forceRedrawAll || obj.redraw) {
					obj.redraw = false;
					// if (obj.glList != -1) {
					// gl.glDeleteLists(obj.glList, 1);
					// }
					if (obj instanceof EmbededObject3D) {
						obj.glList = ((EmbededObject3D) obj)
								.createCallList(gl, enableDrawSolid,
										enableLighting, enableTextures);
					} else if (obj instanceof OBJModel) {
						obj.glList = ((OBJModel) obj).drawToList(gl);
					} else if (obj instanceof Object3D) {
						if (obj instanceof Tegr3dObjectLoader) {
							if (obj.glList != -1) {
								gl.glDeleteLists(obj.glList, 1);
							}
							try {
								obj.glList = ((Tegr3dObjectLoader) obj)
										.createCallList(gl, enableDrawSolid,
												enableTextures, enableLighting,
												drawNormals, drawSmooth,
												drawBoundingBox);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
						// else {
						// obj.glList = obj.createCallList(gl);
						// }
					}
					// System.out.println("redrawing...");
				}
			}
		}

		forceRedrawAll = false;
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		GL gl = drawable.getGL();

		// Set projection matrix
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		float h = (float) height / (float) width;
		gl.glViewport(0, 0, width, height);
		if (enablePerspective) {
			gl.glFrustum(-1, 1, -h, h, PROJECTION_NEAR, PROJECTION_FAR);
		} else {
			gl.glOrtho(-1, 1, -h, h, PROJECTION_NEAR, PROJECTION_FAR);
		}
		GLU glu = new GLU();
		glu.gluLookAt(camera.xPos, camera.yPos, camera.zPos, camera.xView,
				camera.yView, camera.zView, camera.xUp, camera.yUp, camera.zUp);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}

	public void display(GLAutoDrawable drawable) {
		if (!selectObject) {
			display_impl(drawable);
		} else {
			GL gl = drawable.getGL();
			GLU glu = new GLU();

			int buffsize = 512;
			double x = (double) prevMouseX, y = (double) prevMouseY;
			int[] viewPort = new int[4];
			IntBuffer selectBuffer = BufferUtil.newIntBuffer(buffsize);
			int hits = 0;
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort, 0);

			gl.glSelectBuffer(buffsize, selectBuffer);
			gl.glRenderMode(GL.GL_SELECT);
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPushMatrix();
			// gl.glLoadIdentity();
			// glu.gluPickMatrix(x, (double) viewPort[3] - y, 5.0d, 5.0d,
			// viewPort, 0);
			// glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);
			// display_impl(drawable);
			gl.glLoadIdentity();
			// para y --> (double) viewPort[3] - y
			// glu.gluPickMatrix(x, y, 5.0d, 5.0d, viewPort, 0);
			glu.gluPickMatrix(x, viewPort[3] - y, 5.0d, 5.0d, viewPort, 0);
			float h = (float) canvas.getHeight() / (float) canvas.getWidth();
			// gl.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
			if (enablePerspective) {
				// gl.glFrustum(-1, 1, -h, h, PROJECTION_NEAR, PROJECTION_FAR);
				glu.gluPerspective(45,
						(float) canvas.getWidth() / canvas.getHeight(),
						PROJECTION_NEAR, PROJECTION_FAR);
			} else {
				gl.glOrtho(-1, 1, -h, h, PROJECTION_NEAR, PROJECTION_FAR);
			}
			glu.gluLookAt(camera.xPos, camera.yPos, camera.zPos, camera.xView,
					camera.yView, camera.zView, camera.xUp, camera.yUp,
					camera.zUp);
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glInitNames();
			display_impl(drawable);
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glFlush();

			// gl.glFlush();
			hits = gl.glRenderMode(GL.GL_RENDER);
			processHits(hits, selectBuffer);
			selectObject = false;
		}
	}

	double[] currentPMatrix = new double[16];

	double[] currentMVMatrix = new double[16];

	double[] currentObjPos = new double[16];

	public void display_impl(GLAutoDrawable drawable) {
		// System.out.println("display" + System.currentTimeMillis());
		// Test whether object have to be rendered again
		if (redrawOnlyModified || forceRedrawAll) {
			init(drawable);
			redrawOnlyModified = false;
			forceRedrawAll = false;
		}

		GLUT glut = new GLUT();

		// Clear color and depth buffer
		GL gl = drawable.getGL();
		gl.glClearColor(1F, 1F, 1F, 0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		if (redrawScene && objectSelected == -1) {
			synchronized (mouseTransformation) {
				if (mouseTransformation.rotation[0] != 0) {
					sceneTransformation.rotation[0] += mouseTransformation.rotation[0];
					mouseTransformation.rotation[0] = 0;
				}
				if (mouseTransformation.rotation[1] != 0) {
					sceneTransformation.rotation[1] += mouseTransformation.rotation[1];
					mouseTransformation.rotation[1] = 0;
					System.out.print(" scene ["
							+ sceneTransformation.rotation[1] + "]");
				}
				if (mouseTransformation.rotation[2] != 0) {
					sceneTransformation.rotation[2] += mouseTransformation.rotation[2];
					mouseTransformation.rotation[2] = 0;
				}
			}
			redrawScene = false;
		}
		gl.glPushMatrix();
		if (sceneTransformation.rotation[0] != 0) {
			gl.glRotatef(sceneTransformation.rotation[0], 1F, 0F, 0F);
		}
		if (sceneTransformation.rotation[1] != 0) {
			gl.glRotatef(sceneTransformation.rotation[1], 0F, 1F, 0F);
		}
		if (sceneTransformation.rotation[2] != 0) {
			gl.glRotatef(sceneTransformation.rotation[2], 0F, 0F, 1F);
		}
		// forceRedrawAll = true;

		synchronized (objs) {
			for (Object3D obj : objs) {
				if (obj.isVisible) {
					// if (obj instanceof Tegr3dObjectLoader) {
					{
						// calculate new position based on input and animation
						gl.glPushMatrix();

						gl.glLoadIdentity();

						// translacion animada
						gl.glRotatef(obj.rot2[0] + obj.ani_translation[0], 1.F,
								0.0F, 0.0F);
						gl.glRotatef(obj.rot2[1] + obj.ani_translation[1], 0.F,
								1.0F, 0.0F);
						gl.glRotatef(obj.rot2[2] + obj.ani_translation[2], 0.F,
								0.0F, 1.0F);

						float xM = 0, yM = 0, zM = 0;
						if (obj.id == objectSelected) {
							synchronized (mouseTransformation) {
								xM = mouseTransformation.translation2[0];
								yM = mouseTransformation.translation2[1];
								zM = mouseTransformation.translation2[2];
							}
						}

						// posicionar objeto en el centro original
						gl.glTranslatef(obj.getBoundingBox().center[0]
								+ obj.transX + obj.ani_position[0] + xM,
								obj.getBoundingBox().center[1] + obj.transY
										+ obj.ani_position[1] + yM,
								obj.getBoundingBox().center[2] + obj.transZ
										+ obj.ani_position[2] + zM);

						// rotate with input
						if (redrawScene && objectSelected == obj.id) {
							synchronized (mouseTransformation) {
								gl.glRotatef(mouseTransformation.rotation[0],
										1.F, 0.0F, 0.0F);
								gl.glRotatef(mouseTransformation.rotation[1],
										0.F, 1.0F, 0.0F);
								gl.glRotatef(mouseTransformation.rotation[2],
										0.F, 0.0F, 1.0F);
							}
						}

						// rotate object
						if (obj.rotX != 0) {
							gl.glRotatef(obj.rotX + obj.ani_rotation[0], 1.F,
									0.0F, 0.0F);
						}
						if (obj.rotY != 0) {
							gl.glRotatef(obj.rotY + obj.ani_rotation[1], 0.F,
									1.0F, 0.0F);
						}
						if (obj.rotZ != 0) {
							gl.glRotatef(obj.rotZ + obj.ani_rotation[2], 0.F,
									0.0F, 1.0F);
						}

						// escalar objeto
						gl.glScalef(obj.getScale()[0], obj.getScale()[1],
								obj.getScale()[2]);

						// trasladar objeto
						gl.glTranslatef(-obj.getBoundingBox().center[0],
								-obj.getBoundingBox().center[1],
								-obj.getBoundingBox().center[2]);

						// gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX,
						// currentMVMatrix,
						// 0);
						gl.glMultMatrixf(
								new float[] { obj.getBoundingBox().center[0],
										obj.getBoundingBox().center[1],
										obj.getBoundingBox().center[2], 1, 0,
										0, 0, 0, 0, 0, 0, 0 }, 0);
						gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, currentObjPos,
								0);
						gl.glPopMatrix();
					}

					boolean collisionDetected = false;
					if (obj.currentCenter[1] < 0) {
						collisionDetected = true;
						obj.ani_enabled = false;
						System.out
								.println("collision detected to floor for object "
										+ obj.id);
						if (!(obj instanceof WaterDrop)) {
							obj.transY += 1;
						}
						// oldObjs.add(obj);
					}
					if (!collisionDetected) {
						for (Object3D obj2 : objs) {
							if (obj != obj2 && obj2 != uncollisionableObj
									&& obj.isVisible && obj2.isVisible) {
								if (Math.pow(
										obj.getBoundingBox().radius
												+ obj2.getBoundingBox().radius,
										2) > (Math.pow(currentObjPos[0]
										- obj2.currentCenter[0], 2))
										+ Math.pow(currentObjPos[1]
												- obj2.currentCenter[1], 2)
										+ Math.pow(currentObjPos[2]
												- obj2.currentCenter[2], 2)) {
									collisionDetected = true;
									System.out
											.println("collision detected for object "
													+ obj.id
													+ " and obj "
													+ obj2.id);
									break;
								}
								//
							}
						}
					}

					if (true) { // TODO: test collision
						// ignore input, inverse animation and recalculate
						// matrix
						gl.glPushMatrix();

						gl.glLoadIdentity();

						// procesar traslación alrededor del centro
						obj.rot2[0] += obj.ani_translation[0];
						obj.rot2[0] = obj.rot2[0] % 360;
						obj.rot2[1] += obj.ani_translation[1];
						obj.rot2[1] = obj.rot2[1] % 360;
						obj.rot2[2] += obj.ani_translation[2];
						obj.rot2[2] = obj.rot2[2] % 360;

						// procesar rotacion de input
						if (redrawScene && objectSelected == obj.id) {
							synchronized (mouseTransformation) {
								if (mouseTransformation.rotation[0] != 0) {
									obj.rotX += mouseTransformation.rotation[0];
									mouseTransformation.rotation[0] = 0;
								}
								if (mouseTransformation.rotation[1] != 0) {
									obj.rotY += mouseTransformation.rotation[1];
									mouseTransformation.rotation[1] = 0;
								}
								if (mouseTransformation.rotation[2] != 0) {
									obj.rotZ += mouseTransformation.rotation[2];
									mouseTransformation.rotation[2] = 0;
								}
							}
							redrawScene = false;
						}

						// procesar rotacion de animacion
						if (obj.ani_enabled) {
							if (obj.ani_rotation[0] != 0) {
								obj.rotX += obj.ani_rotation[0];
								obj.rotX = obj.rotX % 360;
							}
							if (obj.ani_rotation[1] != 0) {
								obj.rotY += obj.ani_rotation[1];
								obj.rotY = obj.rotY % 360;
							}
							if (obj.ani_rotation[2] != 0) {
								obj.rotZ += obj.ani_rotation[2];
								obj.rotZ = obj.rotZ % 360;
							}
						}

						// procesar desplazamiento de animación
						if (obj.ani_enabled) {
							obj.transX += obj.ani_position[0];
							obj.transY += obj.ani_position[1];
							obj.transZ += obj.ani_position[2];
						}

						if (obj.id == objectSelected) {
							if (!collisionDetected) {
								// System.out.println("transforming object ["
								// + objectSelected + "]...");
								synchronized (mouseTransformation) {
									obj.transX += mouseTransformation.translation2[0];
									obj.transY += mouseTransformation.translation2[1];
									obj.transZ += mouseTransformation.translation2[2];
									mouseTransformation.translation2[0] = 0;
									mouseTransformation.translation2[1] = 0;
									mouseTransformation.translation2[2] = 0;
								}
							} else {
								System.out.println("collision detected ["
										+ objectSelected + "]...");
								synchronized (mouseTransformation) {
									mouseTransformation.translation2[0] = 0;
									mouseTransformation.translation2[1] = 0;
									mouseTransformation.translation2[2] = 0;
								}
							}
						}

						// -----------------------------------------------

						// trasladar alrededor del centro
						gl.glRotatef(obj.rot2[0], 1.F, 0.0F, 0.0F);
						gl.glRotatef(obj.rot2[1], 0.F, 1.0F, 0.0F);
						gl.glRotatef(obj.rot2[2], 0.F, 0.0F, 1.0F);

						// trasladar al puesto original
						gl.glTranslatef(obj.getBoundingBox().center[0]
								+ obj.transX, obj.getBoundingBox().center[1]
								+ obj.transY, obj.getBoundingBox().center[2]
								+ obj.transZ);

						// rotar
						if (obj.rotX != 0) {
							gl.glRotatef(obj.rotX, 1.F, 0.0F, 0.0F);
						}
						if (obj.rotY != 0) {
							gl.glRotatef(obj.rotY, 0.F, 1.0F, 0.0F);
						}
						if (obj.rotZ != 0) {
							gl.glRotatef(obj.rotZ, 0.F, 0.0F, 1.0F);
						}

						// scale object
						gl.glScalef(obj.getScale()[0], obj.getScale()[1],
								obj.getScale()[2]);

						// desplazar
						gl.glTranslatef(-obj.getBoundingBox().center[0],
								-obj.getBoundingBox().center[1],
								-obj.getBoundingBox().center[2]);

						// obtain transformation matrix
						gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX,
								currentMVMatrix, 0);

						gl.glPushMatrix();
						// calculate new center position
						gl.glLoadMatrixd(currentMVMatrix, 0);
						gl.glMultMatrixf(
								new float[] { obj.getBoundingBox().center[0],
										obj.getBoundingBox().center[1],
										obj.getBoundingBox().center[2], 1, 0,
										0, 0, 0, 0, 0, 0, 0 }, 0);
						gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, currentObjPos,
								0);
						obj.currentCenter[0] = currentObjPos[0];
						obj.currentCenter[1] = currentObjPos[1];
						obj.currentCenter[2] = currentObjPos[2];
						gl.glPopMatrix();

						gl.glCallList(obj.createCallList(gl));

						gl.glPopMatrix();
					} else {
						// update state
					}

					// Update
					gl.glPushMatrix();
					gl.glLoadMatrixd(currentMVMatrix, 0);
					if (!collisionDetected) {
						gl.glCallList(obj.createCallList(gl));
					} else {
						// if (obj instanceof WaterDrop) {
						// oldObjs.add(obj);
						// gl.glCallList(obj.createCallList(gl));
						// } else {
						// TODO: hacer algo con este objeto
						// obj.isVisible = false;
						obj.ani_position = new float[] { 0F, 0F, 0F };
						obj.ani_rotation = new float[] { 0F, 0F, 0F };
						obj.ani_translation = new float[] {
								-obj.ani_translation[0],
								-obj.ani_translation[1],
								-obj.ani_translation[2] };
						// obj.ani_enabled = false;
						gl.glCallList(obj.createCallList(gl));
						// }
					}
					if (drawNormals)
						gl.glCallList(clAxis);
					if (objectSelected == obj.id) {
						obj.drawBoundingBox(gl, objectSelectedColor, 1F);
					}
					gl.glPopMatrix();

					// Draw center and bounding sphere
					if (drawBoundingBox) {
						gl.glPushMatrix();
						gl.glLoadIdentity();
						// gl.glLoadMatrixd(currentMVMatrix, 0);
						// gl.glTranslated(currentObjPos[0], currentObjPos[1],
						// currentObjPos[2]);
						// gl.glLoadMatrixd(currentMVMatrix, 0);
						gl.glTranslated(obj.currentCenter[0],
								obj.currentCenter[1], obj.currentCenter[2]);
						glut.glutWireSphere(obj.getBoundingBox().radius, 20, 16);
						gl.glPopMatrix();

						gl.glPushMatrix();
						gl.glLoadIdentity();
						gl.glPointSize(5.0F);
						gl.glColor3f(0.5F, 0.8F, 0.8F);
						gl.glBegin(GL.GL_POINTS);
						gl.glVertex3dv(obj.currentCenter, 0);
						gl.glEnd();
						gl.glPopMatrix();
					}
					// } else {
					// gl.glPushMatrix();
					// gl.glCallList(obj.glList);
					// gl.glPopMatrix();
					// }
				}
			}
		}

		if (enableLighting) {
			for (Light light : lights) {
				if (light.enabled) {
					gl.glPushMatrix();
					gl.glLoadIdentity();
					if (redrawScene && objectSelected == light.glName) {
						synchronized (mouseTransformation) {
							if (mouseTransformation.translation[0] != 0) {
								light.light_position[0] += mouseTransformation.translation[0];
								mouseTransformation.translation[0] = 0;
							}
							if (mouseTransformation.translation[1] != 0) {
								light.light_position[1] += mouseTransformation.translation[1];
								mouseTransformation.translation[1] = 0;
							}
						}
						redrawScene = false;
					}

					gl.glTranslatef(light.light_position[0],
							light.light_position[1], light.light_position[2]);
					gl.glColor3f(1.0F, 1.0F, 0.0F);
					gl.glMaterialfv(GL.GL_FRONT_AND_BACK,
							GL.GL_AMBIENT_AND_DIFFUSE, new float[] { 1.0F,
									1.0F, 0.0F }, 0);
					gl.glBegin(GL.GL_LINES);
					// gl.glVertex3fv(light.light_position, 0);
					gl.glVertex3f(0F, 0F, 0F);
					gl.glVertex3f(light.spot_direction[0],
							light.spot_direction[1], light.spot_direction[2]);
					gl.glEnd();
					if (objectSelected == light.glName) {
						lightBulb.drawBoundingBox(gl, objectSelectedColor, 1F);
					}
					gl.glPushName(light.glName);
					gl.glCallList(clLight);
					gl.glPopName();
					gl.glPopMatrix();
				}
			}
		} else {
			gl.glDisable(GL.GL_LIGHTING);
		}

		gl.glPopMatrix();

		// if (!oldObjs.isEmpty()) {
		// synchronized (oldObjs) {
		// if (oldObjs.size() > 50) {
		// System.out.println("Deleting old objects...");
		// int i = 0;
		// for (Iterator it = oldObjs.iterator(); it.hasNext()
		// && i < 10; i++) {
		// Object a = it.next();
		// objs.remove(a);
		// it.remove();
		// }
		// }
		// }
		// }

		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	public void keyTyped(KeyEvent e) {
	}

	boolean shiftKeyOn = false;

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			runExit();
			break;
		case KeyEvent.VK_E:
			if (objectSelected == -1 || !shiftKeyOn) {
				camera.MoveCamera(Camera.UP);
			} else if (shiftKeyOn) {
				// for (Object3D obj : objs) {
				// if (obj.id == objectSelected) {
				// obj.transZ -= 0.1F;
				// break;
				// }
				// }
				synchronized (mouseTransformation) {
					mouseTransformation.translation2[2] -= 0.5F;
				}
			}
			break;
		case KeyEvent.VK_D:
			if (objectSelected == -1 || !shiftKeyOn) {
				camera.MoveCamera(Camera.DOWN);
			} else if (shiftKeyOn) {
				// for (Object3D obj : objs) {
				// if (obj.id == objectSelected) {
				// obj.transZ += 0.1F;
				// break;
				// }
				// }
				synchronized (mouseTransformation) {
					mouseTransformation.translation2[2] += 0.5F;
				}
			}
			break;
		case KeyEvent.VK_F:
			if (objectSelected == -1 || !shiftKeyOn) {
				camera.StrafeCam(Camera.STRAFE_RIGHT);
			} else if (shiftKeyOn) {
				// for (Object3D obj : objs) {
				// if (obj.id == objectSelected) {
				// obj.transX += 0.1F;
				// break;
				// }
				// }
				synchronized (mouseTransformation) {
					mouseTransformation.translation2[0] += 0.5F;
				}
			}
			break;
		case KeyEvent.VK_S:
			if (objectSelected == -1 || !shiftKeyOn) {
				camera.StrafeCam(Camera.STRAFE_LEFT);
			} else if (shiftKeyOn) {
				// for (Object3D obj : objs) {
				// if (obj.id == objectSelected) {
				// obj.transX -= 0.1F;
				// break;
				// }
				// }
				synchronized (mouseTransformation) {
					mouseTransformation.translation2[0] -= 0.5F;
				}
			}
			break;
		case KeyEvent.VK_J:
			camera.Rotate(-1, 0);
			break;
		case KeyEvent.VK_L:
			camera.Rotate(1, 0);
			break;
		case KeyEvent.VK_I:
			camera.Rotate(0, -1);
			break;
		case KeyEvent.VK_K:
			camera.Rotate(0, 1);
			break;
		case KeyEvent.VK_T:
			break;
		case KeyEvent.VK_G:
			break;
		case KeyEvent.VK_R:
			if (objectSelected == -1 || !shiftKeyOn) {
				camera.SetCamera(camera.xPos, camera.yPos + 0.5F, camera.zPos,
						camera.xView, camera.yView + 0.5F, camera.zView,
						camera.zUp, camera.yUp, camera.zUp);
			} else if (shiftKeyOn) {
				// for (Object3D obj : objs) {
				// if (obj.id == objectSelected) {
				// obj.transX -= 0.1F;
				// break;
				// }
				// }
				synchronized (mouseTransformation) {
					System.out.println("Moving up!");
					mouseTransformation.translation2[1] += 0.5F;
				}
			}
			break;
		case KeyEvent.VK_V:
			if (objectSelected == -1 || !shiftKeyOn) {
				camera.SetCamera(camera.xPos, camera.yPos - 0.5F, camera.zPos,
						camera.xView, camera.yView - 0.5F, camera.zView,
						camera.zUp, camera.yUp, camera.zUp);
			} else if (shiftKeyOn) {
				synchronized (mouseTransformation) {
					mouseTransformation.translation2[1] -= 0.5F;
				}
			}
			break;
		case KeyEvent.VK_C:
			camera.SetCamera(cameraPos[0], cameraPos[1], cameraPos[2],
					WORLD_CENTER[0], WORLD_CENTER[1], WORLD_CENTER[2], 0.0F,
					1.0F, 0.0F);
			sceneTransformation.rotation = new float[] { 0F, 0F, 0F };
			sum = 0;
			break;
		case KeyEvent.VK_1:
			enableDrawSolid = !enableDrawSolid;
			forceRedrawAll = true;
			break;
		case KeyEvent.VK_2:
			enableLighting = !enableLighting;
			forceRedrawAll = true;
			break;
		case KeyEvent.VK_3:
			enableTextures = !enableTextures;
			forceRedrawAll = true;
			break;
		case KeyEvent.VK_SHIFT:
			shiftKeyOn = true;
			break;
		case KeyEvent.VK_N:
			// TODO: schedule rainTask
			timer.schedule(rainTask, 5000, 1000);
			break;
		}
		sceneControlPanel.update();

		// canvas.repaint();
		// redrawAll = true;
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SHIFT:
			shiftKeyOn = false;
			break;
		}
	}

	public void mouseClicked(MouseEvent e) {
		prevMouseX = e.getX();
		prevMouseY = e.getY();
		System.out.println("Selecting object at [" + e.getX() + "],["
				+ e.getY() + "]...");
		selectObject = true;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	int prevMouseX;

	int prevMouseY;

	public void mousePressed(MouseEvent e) {
		prevMouseX = e.getX();
		prevMouseY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
	}

	float sum = 0;

	public void mouseDragged(MouseEvent e) {
		int incX = (e.getX() - prevMouseX);
		int incY = (e.getY() - prevMouseY);
		prevMouseX = e.getX();
		prevMouseY = e.getY();

		if (shiftKeyOn) {
			synchronized (mouseTransformation) {
				mouseTransformation.rotation[0] += incY * (float) 360
						/ (float) canvas.getHeight();
				mouseTransformation.rotation[1] += ((float) incX)
						/ ((float) canvas.getWidth()) * ((float) 360);
				sum += mouseTransformation.rotation[1];
				System.out.println("sum: " + sum);
				mouseTransformation.translation[0] = incX / 10;
				mouseTransformation.translation[1] = -incY / 10;

			}
			redrawScene = true;
			forceRedrawAll = enableLighting;
		} else {
			camera.Rotate(
					(float) ((float) incX / (float) canvas.getWidth() * 2 * Math.PI),
					(float) (((float) incY / (float) canvas.getHeight() * 2 * Math.PI)));
		}

		canvas.reshape(0, 0, canvas.getWidth(), canvas.getHeight());
		// canvas.swapBuffers();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void runExit() {
		// Run this on another thread than the AWT event queue to
		// make sure the call to Animator.stop() completes before
		// exiting
		new Thread(new Runnable() {
			public void run() {
				animator.stop();
				try {
					EventQueue.invokeAndWait(new Runnable() {
						public void run() {
							if (fullScreen) {
								try {
									dev.setDisplayMode(origMode);
								} catch (Exception e1) {
								}
								try {
									dev.setFullScreenWindow(null);
								} catch (Exception e2) {
								}
								fullScreen = false;
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		}).start();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		runExit();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void objectUpdated(Object3D obj) {
		objectsPanel.update();
		obj.redraw = true;
		redrawOnlyModified = true;
	}

	public void loadObject(Object3D obj) {
		obj.reshape(WORLD_CENTER, 1, 1, 1);
		obj.isVisible = true;
		obj.redraw = true;

		objs.add(obj);
		// objsCLs.add(-1);
		objectsPanel.reload();
		redrawOnlyModified = true;
	}

	public void reshape() {
		canvas.reshape(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("exit")) {
			runExit();
		} else if (actionCommand.equals("loadObject")) {
			Object3DLoadDialog dialog = new Object3DLoadDialog(this);
			dialog.setVisible(true);
		} else if (actionCommand.equals("changeDisplay")) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					showChangeDisplayModeDialog();
				}
			});
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		Dimension size = e.getComponent().getSize();
		int notches = e.getWheelRotation();
		if (notches < 0) {
			camera.MoveCamera(Camera.UP);
		} else {
			camera.MoveCamera(Camera.DOWN);
		}
		canvas.reshape(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void processHits(int hits, IntBuffer buffer) {
		System.out.println("---------------------------------");
		System.out.println(" HITS: " + hits);
		int offset = 0;
		int names;
		float z1, z2;
		objectSelected = -1;
		for (int i = 0; i < hits && objectSelected == -1; i++) {
			System.out.println("- - - - - - - - - - - -");
			System.out.println(" hit: " + i);
			names = buffer.get(offset);
			offset++;
			System.out.print("z1[" + buffer.get(offset) + "]->");
			z1 = (float) buffer.get(offset) / (0xffffffff);
			offset++;
			System.out.print("z2[" + buffer.get(offset) + "]->");
			z2 = (float) buffer.get(offset) / (0xffffffff);
			offset++;
			System.out.println(" number of names: " + names);
			System.out.println(" z1: " + z1);
			System.out.println(" z2: " + z2);
			System.out.println(" names: ");

			for (int j = 0; j < names; j++) {
				int id = buffer.get(offset);
				System.out.print("       " + id);
				if (j == (names - 1)) {
					System.out.println("<-");
					if (unselectableObjs == null || unselectableObjs.id != id) {
						objectSelected = id;
						break;
					}

				} else
					System.out.println();
				offset++;
			}
			System.out.println("- - - - - - - - - - - -");
		}
		System.out.println("---------------------------------");
		System.out.println("Object selected [" + objectSelected + "]");
	}

	public class RainTask extends TimerTask {

		RainTask() {
		}

		public void run() {
			for (int i = 0; i < 10; i++) {
				WaterDrop drop = new WaterDrop();
				drop.transX = (float) Math.random() * 10 - 5;
				drop.transY = 10;
				drop.transZ = (float) Math.random() * 10 - 5;
				drop.ani_enabled = true;
				drop.ani_position[1] = -0.1F;
				drop.isVisible = true;
				newObjs.add(drop);
				synchronized (objs) {
					objs.add(drop);
				}
			}
			if (newObjs.size() > 200) {
				int i = 0;
				for (Iterator it = newObjs.iterator(); it.hasNext() && i < 10; i++) {
					Object o = it.next();
					synchronized (objs) {
						objs.remove(o);
					}
					it.remove();
				}
			}
		}
	}
}
