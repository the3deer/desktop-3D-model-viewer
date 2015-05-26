package org.andresoviedo.app.modelviewer3D.loaders.autocad;
//package org.andresoviedo.app.modelviewer3D.loaders.autocad;
//
//import java.io.IOException;
//
//import javax.media.opengl.GLAutoDrawable;
//
//import org.jouvieje.model.Model;
//import org.jouvieje.model.light.Lighting.LightingMode;
//import org.jouvieje.model.material.ModelTextureList;
//import org.jouvieje.model.reader.ModelReaderException;
//import org.jouvieje.model.reader.ModelReaderSettings;
//import org.jouvieje.model.reader.UnifiedReader;
//import org.jouvieje.model.renderer.pipeline.DLRenderer_Model;
//import org.jouvieje.model.ressources.MediaManager;
//import org.jouvieje.renderer.jsr231.GLRenderer_jsr231;
//import org.jouvieje.texture.TextureLoader;
//import org.jouvieje.visibility.BoundingBox.BoundingBoxType;
//
//public class AutocadLoader {
//
//	public Model createModel(GLAutoDrawable drawable, String folder,
//			String fileName, boolean lightOn, boolean textureOn)
//			throws ModelReaderException, IOException {
//		Model ret = null;
//		GLRenderer_jsr231 opengl = new GLRenderer_jsr231(null);
//		opengl.update(drawable);
//
//		ModelReaderSettings settings = new ModelReaderSettings();
//		// settings.modelFolder = "/models/obj/XWing/";
//		// settings.modelName = "XWing.obj";
//		settings.modelFolder = folder;
//		// settings.modelName = "XWing.obj";
//		settings.modelName = fileName;
//		settings.lighting = lightOn ? LightingMode.PER_VERTEX : LightingMode.NO;
//		settings.debug = false;
//
//		/*
//		 * Load the model MediaManager is to holds texture and
//		 */
//		ret = new Model(new MediaManager());
//		new UnifiedReader().read(ret, settings);
//		/*
//		 * Load all OpenGL context dependant datas: textures, shaders ...
//		 */
//		if (textureOn) {
//			ret.medias.textures = new ModelTextureList(
//					new TextureLoader(opengl));
//			ret.medias.textures.loadTextures(ret);
//		}
//
//		/*
//		 * Renderer ======== Here is the list of advised renderers from the
//		 * slowest to fastest : DirectModeRenderer Model is rendered in Direct
//		 * Model (glBegin/glEnd) DLRenderer Model is rendered with DisplayList
//		 * VertexArrayRenderer Model is rendered with VertexArray VBORenderer
//		 * Model is rendered with VertexBufferedObject (VBO)
//		 */
//		// model.renderer = new DirectModeRenderer(opengl, model,
//		// boundingVolumeType); //Should be used only for debug
//		// model.renderer = new DLRenderer(opengl, model,
//		// boundingVolumeType);
//		ret.renderer = new DLRenderer_Model(opengl, ret,
//				BoundingBoxType.BoundingBox_OBB_Visible_ObjectOnly);
//		// model.renderer = new VBORenderer(opengl, model,
//		// boundingVolumeType);
//		return ret;
//	}
//}
