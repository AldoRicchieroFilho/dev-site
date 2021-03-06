/*
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.site.demo.gwtchosen.sample;

import com.arcbees.chosen.client.ChosenOptions;
import com.arcbees.chosen.client.DropdownPosition;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import static com.arcbees.chosen.client.Chosen.Chosen;
import static com.google.gwt.query.client.GQuery.$;

public class ChosenOptionsWidget implements IsWidget {
    @UiTemplate("ChosenOptionsWidget.ui.xml")
    interface Binder extends UiBinder<Widget, ChosenOptionsWidget> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    SelectElement allowSingleDeselect;
    @UiField
    SelectElement disableSearchThreshold;
    @UiField
    SelectElement searchContains;
    @UiField
    SelectElement singleBackstrokeDelete;
    @UiField
    SelectElement maxSelectedOptions;
    @UiField
    SelectElement noResultsText;
    @UiField
    SelectElement dropdownPosition;
    @UiField
    SelectElement mobileWidth;
    @UiField
    SelectElement mobileAnimation;
    @UiField
    SelectElement mobileSpeed;

    private final Widget widget;

    public ChosenOptionsWidget() {
        widget = uiBinder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    $(allowSingleDeselect).as(Chosen).chosen(
                            new ChosenOptions().setAllowSingleDeselect(true));

                    $(disableSearchThreshold).as(Chosen).chosen(
                            new ChosenOptions().setDisableSearchThreshold(10));

                    $(searchContains).as(Chosen).chosen(
                            new ChosenOptions().setSearchContains(true));

                    $(singleBackstrokeDelete).as(Chosen).chosen(
                            new ChosenOptions().setSingleBackstrokeDelete(true));

                    $(maxSelectedOptions).as(Chosen).chosen(
                            new ChosenOptions().setMaxSelectedOptions(5));

                    $(noResultsText).as(Chosen).chosen(
                            new ChosenOptions().setNoResultsText("Ooops, nothing was found:"));

                    $(dropdownPosition).as(Chosen).chosen(
                            new ChosenOptions().setDropdownPosition(DropdownPosition.ABOVE));

                    $(mobileWidth).as(Chosen).chosen(
                            new ChosenOptions().setMobileViewportMaxWidth(2000));

                    $(mobileAnimation).as(Chosen).chosen(
                            new ChosenOptions().setMobileAnimation(false));

                    $(mobileSpeed).as(Chosen).chosen(
                            new ChosenOptions().setMobileAnimationSpeed(1500));
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
