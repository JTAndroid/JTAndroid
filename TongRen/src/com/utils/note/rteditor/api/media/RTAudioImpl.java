/*
 * Copyright (C) 2015 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utils.note.rteditor.api.media;

/**
 * This is a basic implementation of the RTAudio interface.
 */
public class RTAudioImpl extends RTMediaImpl implements RTAudio {
    private static final long serialVersionUID = -1213141231761752521L;

    private String mAudioPreviewImage;

    private String mAudioPath;

    public RTAudioImpl(String imagePath,String audioPath) {
        super(imagePath,audioPath);
        setAudioPath(audioPath);
    }

    @Override
    public void remove() {
        super.remove();
        removeFile(mAudioPreviewImage);
    }

    @Override
    public void setAudioPreviewImage(String audioPreviewImage) {
        mAudioPreviewImage = audioPreviewImage;
    }

    @Override
    public void setAudioPath(String path) {
        mAudioPath = path;
    }

    public String getAudioPath(){
        return mAudioPath;
    }

    @Override
    public String getAudioPreviewImage() {
        return mAudioPreviewImage;
    }

    @Override
    public int getHeight() {
        return getHeight(mAudioPreviewImage);
    }

    @Override
    public String getMediaPath() {
        return mAudioPath;
    }

    @Override
    public int getWidth() {
        return getWidth(mAudioPreviewImage);
    }

}