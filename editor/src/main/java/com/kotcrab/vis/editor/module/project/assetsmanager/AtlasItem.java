/*
 * Copyright 2014-2016 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kotcrab.vis.editor.module.project.assetsmanager;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.runtime.assets.AtlasRegionAsset;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;

/**
 * Displays single region from texture atlas
 * @author Kotcrab
 */
public class AtlasItem extends Table {
	private AtlasRegionAsset assetDescriptor;
	private String relativeAtlasPath;
	private AtlasRegion region;

	private ShapeRenderer shapeRenderer;

	private boolean isSelected;

	public AtlasItem (String relativeAtlasPath, AtlasRegion region, boolean showLabel) {
		super(VisUI.getSkin());
		this.relativeAtlasPath = relativeAtlasPath;
		this.region = region;

		isSelected = false;
		shapeRenderer = new ShapeRenderer();
		assetDescriptor = new AtlasRegionAsset(relativeAtlasPath, region.name);

		setTouchable(Touchable.enabled);
		setBackground("menu-bg");

		Image img = new Image(region);
		img.setScaling(Scaling.fit);
		add(img).expand().fill().row();

		if (showLabel) {
			VisLabel name = new VisLabel(region.name, "small");
			name.setWrap(true);
			name.setAlignment(Align.center);
			add(name).expandX().fillX();
		}

		addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				isSelected = !isSelected;
			}
		});
	}

	public AtlasRegionAsset getAtlasAsset () {
		return assetDescriptor;
	}

	public AtlasRegion getRegion () {
		return region;
	}

	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (isSelected) {
			Color color = getColor();
			batch.setColor(1f, 1f, 1f, color.a * parentAlpha);

			float x = getX();
			float y = getY();
			float width = getWidth();
			float height = getHeight();

			float halfBorderWidth = 1f;

			getBorderDrawable().draw(batch, x, y + height - halfBorderWidth, width, halfBorderWidth);
			// Bottom border
			getBorderDrawable().draw(batch, x, y, width, halfBorderWidth);
			// Left border
			getBorderDrawable().draw(batch, x, y, halfBorderWidth, height);
			// Right border
			getBorderDrawable().draw(batch, x + width - halfBorderWidth, y, halfBorderWidth, height);
		}
	}

	private Drawable getBorderDrawable() {
		return new SolidDrawable(Color.WHITE);
	}

	private static class SolidDrawable implements Drawable {

		private final TextureRegionDrawable drawable;

		public SolidDrawable(Color color) {
			drawable = new TextureRegionDrawable();
			drawable.setRegion(createRegion(color));
		}

		private static TextureRegion createRegion(Color color) {
			Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pixmap.setColor(color);
			pixmap.fill();
			TextureRegion textureRegion = new TextureRegion(new Texture(pixmap));
			pixmap.dispose();
			return textureRegion;
		}

		@Override
		public void draw(Batch batch, float x, float y, float width, float height) {
			drawable.draw(batch, x, y, width, height);
		}

		@Override
		public float getLeftWidth() {
			return 0;
		}

		@Override
		public void setLeftWidth(float leftWidth) {
		}

		@Override
		public float getRightWidth() {
			return 0;
		}

		@Override
		public void setRightWidth(float rightWidth) {
		}

		@Override
		public float getTopHeight() {
			return 0;
		}

		@Override
		public void setTopHeight(float topHeight) {
		}

		@Override
		public float getBottomHeight() {
			return 0;
		}

		@Override
		public void setBottomHeight(float bottomHeight) {
		}

		@Override
		public float getMinWidth() {
			return 0;
		}

		@Override
		public void setMinWidth(float minWidth) {
		}

		@Override
		public float getMinHeight() {
			return 0;
		}

		@Override
		public void setMinHeight(float minHeight) {
		}
	}
}
