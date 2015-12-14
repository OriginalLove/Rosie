/*
 * Copyright (C) 2015 Karumi.
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

package com.karumi.rosie.sample.comics.view.renderer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.karumi.rosie.renderer.RosieRenderer;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.view.presenter.ComicsPresenter;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;

public class ComicRenderer extends RosieRenderer<ComicViewModel> {

  private final ComicsPresenter presenter;

  @Bind(R.id.tv_comic_name) TextView nameView;

  public ComicRenderer(ComicsPresenter presenter) {
    this.presenter = presenter;
  }

  @Override public void render() {
    super.render();

    ComicViewModel comic = getContent();
    nameView.setText(comic.getTitle());
  }

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(R.layout.item_comic, parent, false);
  }

  @OnClick(R.id.ll_root) public void onItemClicked() {
    ComicViewModel comic = getContent();
    presenter.onComicClicked(comic);
  }
}