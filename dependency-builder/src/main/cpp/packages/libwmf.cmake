include_guard(GLOBAL)

ExternalProjectAutotools(libwmf
  DEPENDS freetype libpng libxml-2.0

  URL https://github.com/caolanm/libwmf/archive/v0.2.12.tar.gz
  URL_HASH SHA256=464ff63605d7eaf61a4a12dbd420f7a41a4d854675d8caf37729f5bc744820e2

  CONFIGURE_ARGUMENTS
    --with-freetype=${THIRDPARTY_PREFIX}
    --with-libxml2=${THIRDPARTY_PREFIX}
  EXTRA_ENVVARS "LIBS=-lpng16 -lm -lz"
)

