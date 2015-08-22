package com.hea3ven.buildingbricks.core.materials;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class MaterialResourceLoader {

	static final Gson GSON = (new GsonBuilder())
			.registerTypeAdapter(Material.class, new MaterialDeserializer())
			.registerTypeAdapter(StructureMaterial.class, new StructureMaterialDeserializer())
			.create();

	public static void discoverMaterials() {
		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();

		Set<ResourceLocation> res = new HashSet<ResourceLocation>();
		for (IResourceManager mgr : getDomainResourceManagers(resourceManager)) {
			for (IResourcePack resPack : getResourcePackages(mgr)) {
				res.addAll(getMaterials(resPack));
			}
		}

		for (ResourceLocation r : res) {
			InputStream matStream = null;
			try {
				matStream = resourceManager.getResource(r).getInputStream();
				Material mat = GSON.fromJson(new InputStreamReader(matStream, Charsets.UTF_8),
						Material.class);
				for (MaterialBlockType blockType : mat.getStructureMaterial().getBlockTypes()) {
					if (mat.getBlock(blockType) == null)
						mat.addBlock(blockType);
				}
				MaterialRegistry.registerMaterial(mat);

			} catch (IOException e) {
				Throwables.propagate(e);
			} finally {
				IOUtils.closeQuietly(matStream);
			}
		}
	}

	private static List<IResourcePack> getResourcePackages(IResourceManager mgr) {
		return MaterialResourceLoader.<List<IResourcePack>> getField(mgr,
				FallbackResourceManager.class, "resourcePacks");
	}

	private static Collection<IResourceManager> getDomainResourceManagers(
			IResourceManager resourceManager) {
		return MaterialResourceLoader
				.<Map<String, IResourceManager>> getField(resourceManager,
						SimpleReloadableResourceManager.class, "domainResourceManagers")
				.values();
	}

	private static Set<ResourceLocation> getMaterials(IResourcePack resPack) {
		Set<ResourceLocation> materials = new HashSet<ResourceLocation>();
		if (resPack instanceof FolderResourcePack) {
			File rootDir = getField(resPack, AbstractResourcePack.class, "resourcePackFile");
			for (File subPath : new File(rootDir, "assets").listFiles()) {
				File materialsDir = new File(subPath, "materials");
				File[] materialFiles = materialsDir
						.listFiles((FileFilter) new WildcardFileFilter("*.json"));
				if (materialFiles != null) {
					for (File materialFile : materialFiles) {
						materials.add(new ResourceLocation(subPath.getName(),
								"materials/" + materialFile.getName()));
					}
				}
			}
		}
		return materials;
	}

	private static <T> T getField(Object obj, Class<?> cls, String fieldName) {
		try {
			Field fld = cls.getDeclaredField(fieldName);
			fld.setAccessible(true);
			return (T) fld.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class MaterialDeserializer implements JsonDeserializer<Material> {

		@Override
		public Material deserialize(JsonElement element, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			JsonObject json = element.getAsJsonObject();

			if (!json.has("id"))
				throw new JsonParseException("material does not have an id");

			Material mat = new Material(json.get("id").getAsString());

			if (!json.has("type"))
				throw new JsonParseException("material does not have a type");
			mat.setStructureMaterial((StructureMaterial) context.deserialize(json.get("type"),
					StructureMaterial.class));

			if (!json.has("textures"))
				throw new JsonParseException("material does not have textures");
			if (json.get("textures").isJsonPrimitive())
				mat.setTexture(json.get("textures").getAsString());
			else {
				JsonObject textures = json.get("textures").getAsJsonObject();
				mat.setTexture(textures.get("top").getAsString(),
						textures.get("bottom").getAsString(), textures.get("side").getAsString());
			}

			for (Entry<String, JsonElement> blockEntry : json
					.get("blocks")
					.getAsJsonObject()
					.entrySet()) {
				MaterialBlockType type = MaterialBlockType
						.valueOf(blockEntry.getKey().toUpperCase());
				if (blockEntry.getValue().isJsonPrimitive()) {
					String blockName = blockEntry.getValue().getAsString();
					mat.addBlock(new BlockDescription(type, Block.getBlockFromName(blockName)));
				} else {
					JsonObject blockJson = blockEntry.getValue().getAsJsonObject();
					String blockName = blockJson.get("id").getAsString();
					int meta = blockJson.get("meta").getAsInt();
					mat.addBlock(
							new BlockDescription(type, Block.getBlockFromName(blockName), meta));
				}
			}

			return mat;
		}

	}

	public static class StructureMaterialDeserializer
			implements JsonDeserializer<StructureMaterial> {

		@Override
		public StructureMaterial deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			String strucMatName = JsonUtils.getJsonElementStringValue(json, "type");
			return StructureMaterial.valueOf(strucMatName.toUpperCase());
		}

	}

}