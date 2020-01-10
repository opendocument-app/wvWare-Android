#include <android/log.h>

#define ParenthesesStripper(...) __VA_ARGS__

#ifdef wvError
#undef wvError
#endif
#define wvError( args ) __android_log_print(ANDROID_LOG_ERROR, "wv", ParenthesesStripper args);

#ifdef wvWarning
#undef wvWarning
#endif
#define wvWarning( args ) __android_log_print(ANDROID_LOG_WARN, "wv", args);

#ifdef wvTrace
#undef wvTrace
#endif
#define wvTrace( args ) __android_log_print(ANDROID_LOG_VERBOSE, "wv", ParenthesesStripper args);
