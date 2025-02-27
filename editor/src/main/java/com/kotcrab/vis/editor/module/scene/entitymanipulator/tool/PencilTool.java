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

package com.kotcrab.vis.editor.module.scene.entitymanipulator.tool;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kotcrab.vis.editor.App;


/** @author Kotcrab */
public class PencilTool extends Tool {
	public static final String TOOL_ID = App.PACKAGE + ".tools.PencilTool";

	@Override
	public void render (ShapeRenderer shapeRenderer) {
		super.render(shapeRenderer);
	}

	@Override
	public String getToolId () {
		return TOOL_ID;
	}
}
