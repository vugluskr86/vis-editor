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

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.editor.ui.SearchField;
import com.kotcrab.vis.editor.ui.tab.CloseTabWhenMovingResources;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import org.lwjgl.Sys;

/**
 * Tab used to display all regions from single texture atlas
 * @author Kotcrab
 */
public class TextureAtlasViewTab extends Tab implements CloseTabWhenMovingResources {
	private String name;

	private VisTable contentTable;
	private VisTable contentTableWrapper;
	private Array<AtlasItem> items = new Array<>();
	private GridGroup filesView;
	private VisTable atlasTable;

	private VisScrollPane scrollPane;

	private int cellInRow = 40;

	public TextureAtlasViewTab (String relativeAtlasPath, TextureAtlas atlas, String name) {
		super(false, true);
		this.name = name;

		atlasTable = new VisTable();
		filesView = new GridGroup(64, 4);

		SearchField searchField = new SearchField(newText -> {
			filesView.clear();

			for (AtlasItem item : items) {
				if (item.getRegion().name.contains(newText))
					filesView.addActor(item);
			}

			if (filesView.getChildren().size == 0)
				return false;
			else
				return true;
		});

		final IntSpinnerModel rowCellCountSpinnerModel = new IntSpinnerModel(40, 1, 999999);

		Spinner rowCellCountSpinner = new Spinner("Cell in row", rowCellCountSpinnerModel);

		VisCheckBox isSplit = new VisCheckBox("Split");
		isSplit.setChecked(false);

		rowCellCountSpinner.setDisabled(true);
		searchField.setVisible(true);

		rowCellCountSpinner.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				cellInRow = rowCellCountSpinnerModel.getValue();
				addRegions(relativeAtlasPath, atlas, true);
			}
		});

		isSplit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (isSplit.isChecked()) {
					rowCellCountSpinner.setDisabled(false);
					searchField.setVisible(false);
				} else {
					rowCellCountSpinner.setDisabled(true);
					searchField.setVisible(true);
				}
				addRegions(relativeAtlasPath, atlas, isSplit.isChecked());
			}
		});

		VisTable topTable = new VisTable(true);
		topTable.add(name);
		topTable.add().expand().fill();
		topTable.add(isSplit);
		topTable.add(rowCellCountSpinner);
		topTable.add(searchField).right().row();

		contentTable = new VisTable();
		contentTable.setBackground(VisUI.getSkin().getDrawable("window-bg"));
		contentTable.add(topTable).expandX().fillX().pad(3).row();
		contentTable.addSeparator().pad(0);

		contentTableWrapper = new VisTable();
		contentTable.add(contentTableWrapper).expand().fill();

		addRegions(relativeAtlasPath, atlas, isSplit.isChecked());
	}

	private void configureScrollPane(boolean isSplit) {
		if (scrollPane != null) {
			contentTableWrapper.removeActor(scrollPane);
		}
		scrollPane = new VisScrollPane(isSplit ? atlasTable : filesView);
		scrollPane.setFlickScroll(false);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setFillParent(true);
		if (isSplit) {
			contentTableWrapper.add(scrollPane).left();
		} else {
			contentTableWrapper.add(scrollPane);
		}
	}

	private void addRegions(String relativeAtlasPath, TextureAtlas atlas, boolean isSplit) {
		atlasTable.clear();
		filesView.clear();
		items.clear();
		configureScrollPane(isSplit);
		Array<AtlasRegion> regions = atlas.getRegions();
		if (isSplit) {
			int index = 0;
			for (AtlasRegion region : regions) {
				AtlasItem item = new AtlasItem(relativeAtlasPath, region, false);
				items.add(item);
				atlasTable.add(item)
						.width(item.getRegion().originalWidth)
						.height(item.getRegion().originalHeight)
						.pad(1.0f);
				if (index % cellInRow == cellInRow - 1) {
					atlasTable.row();
				}
				index += 1;
			}
		} else {
			for (AtlasRegion region : regions) {
				AtlasItem item = new AtlasItem(relativeAtlasPath, region, false);
				items.add(item);
				filesView.addActor(item);
			}
		}
	}

	public Array<AtlasItem> getItems () {
		return items;
	}

	@Override
	public String getTabTitle () {
		return name;
	}

	@Override
	public Table getContentTable () {
		return contentTable;
	}

	@Override
	public void reopenSelfAfterAssetsUpdated () {
		//TODO reopen self
	}
}
