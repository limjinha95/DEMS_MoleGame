#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <time.h>

#define HEX_DOTMATRIX 20

static int running = 1;
static int check;
static int input_num;


int make_mole(char* result) {
	int hex_base[6] = { 0x3f, 0x21, 0x21, 0x21, 0x21, 0x3f };
	int mod_col[4] = { 0x23, 0x25, 0x29, 0x31 };
	int row, col;

	srand((unsigned)time(NULL));
	col = rand() % 4 + 1;
	row = rand() % 4;

	hex_base[col] = mod_col[row];

	for(i = 0; i < HEX_DOTMATRIX; i++) result[i] = '0';
	for(i = 0; i < 6; i++) {
		sprintf(result, "%x%x", hex_base[i] / 16, hex_base[i] % 16);
	}
	return row*4 + col;

}

void* input_thread(void* arg) {
	int dev[2];
	int i;

	dev[0] = open("/dev/fpga_keyapd12", O_RDWR);
	dev[1] = open("/dev/fpga_piezo", O_WRONLY);

	while(running) {


	}

	for(i = 0; i < 2; i++) {
		close(dev[i]);
	}

	 pthread_exit(0);
}

int main(int argc, char * argv[]) {
	pthread_t tid;
	int dev;

	dev = open("/dev/fpga_dotmatrix12", O_WRONLY);

	pthread_create(&tid, NULL, input_thread, NULL);
}
