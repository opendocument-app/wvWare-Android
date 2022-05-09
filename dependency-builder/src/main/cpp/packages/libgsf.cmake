include_guard(GLOBAL)

#@TODO: enable largefile for android-24 ? or Android64?
#https://android.googlesource.com/platform/bionic/+/master/docs/32-bit-abi.md

ExternalProjectAutotools(libgsf
  DEPENDS glib-2.0 libxml-2.0 zlib

  URL https://ftp.gnome.org/pub/GNOME/sources/libgsf/1.14/libgsf-1.14.49.tar.xz
  URL_HASH SHA256=e9ebe36688f010c9e6e40c8903f3732948deb8aca032578d07d0751bd82cf857

  CONFIGURE_ARGUMENTS --disable-largefile
)

