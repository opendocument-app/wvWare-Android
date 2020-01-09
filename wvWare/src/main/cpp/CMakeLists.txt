# CMakeLists.txt
#
# Copyright (C) 2020 Vilius Sutkus'89 <ViliusSutkus89@gmail.com>
#
# wvWare-Android is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 3 as published by
# the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

cmake_minimum_required(VERSION 3.10.2 FATAL_ERROR)
set(CMAKE_BUILD_TYPE Release CACHE STRING "Build configuration (Debug, Release, RelWithDebInfo, MinSizeRel)")

project(wvWare-Android)

get_filename_component(GIT_ROOT_DIR "${CMAKE_CURRENT_LIST_DIR}/../../../.." ABSOLUTE)
SET(PKG_CONFIG_EXECUTABLE ${GIT_ROOT_DIR}/dependency-builder/build/installed/${CMAKE_BUILD_TYPE}/${ANDROID_ABI}/bin/pkg-config)

include(FindPkgConfig)

find_library(log-lib log)

add_library(tmpfile SHARED IMPORTED)
set_property(TARGET tmpfile PROPERTY IMPORTED_LOCATION
        # libtmpfile.so will be extracted in this directory by
        # task extractLibtmpfileSoForLinkingInCMake in sampleapp/app/build.gradle
        ${CMAKE_CURRENT_LIST_DIR}/../../../build/tmpfile/jni/${ANDROID_ABI}/libtmpfile.so
        )

pkg_search_module(wvWare REQUIRED wv-1.0)

link_directories(${wvWare_LIBRARY_DIRS})

add_library(wvware-android SHARED wvWare-Android.cpp
        CCharGC.h
        wvWare.c
        )

target_include_directories(wvware-android PRIVATE ${wvWare_INCLUDE_DIRS})

target_link_libraries(wvware-android ${wvWare_LIBRARIES} ${log-lib} tmpfile)
