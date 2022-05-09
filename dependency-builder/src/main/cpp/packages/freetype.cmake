include_guard(GLOBAL)

ExternalProjectAutotools(freetype
  DEPENDS libpng zlib
  URL https://download.sourceforge.net/freetype/freetype-2.12.1.tar.xz
  URL_HASH SHA256=4766f20157cc4cf0cd292f80bf917f92d1c439b243ac3018debf6b9140c41a7f

  CONFIGURE_ARGUMENTS --with-zlib=yes --with-bzip2=no
    --with-png=yes
    --with-harfbuzz=no
    --enable-freetype-config
)

