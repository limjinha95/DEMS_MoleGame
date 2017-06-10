.SILENT:

include C:/AndroXStudio/Tools/make_comm

all: $(OBJECTS)
	$(CC) $(CFLAGS) $(LDFLAGS) -save-temps -march=armv7-a -O2 -o project $(OBJECTS) \
		$(LLIB)/libgcc.a

	echo 
	echo "**** Install:" /data/local/tmp/project "****"
	$(ADB) push project /data/local/tmp
	$(ADB) shell chmod 755 /data/local/tmp/project
	echo 
	echo "**** Run ****"
	$(ADB) shell TIMEFORMAT="" time /data/local/tmp/project

clean:
	rm -f *.o
	rm -f *.i
	rm -f *.s
	rm -f project
