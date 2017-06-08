#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/fs.h>
#include <linux/miscdevice.h>
#include <mach/hardware.h>
#include <asm/uaccess.h>
#include <linux/fs.h>
#include <linux/types.h>
#include <asm/ioctl.h>
#include <linux/ioport.h>
#include <asm/io.h>


#define DRIVER_AUTHOR " EMS Design"
#define DRIVER_DESC "KEYPAD program"
#define KEYPAD_NAME "fpga_keypad12"
#define KEYPAD_PHY_ADDR 0x05000000
#define KEYPAD_ADDR_RANGE 0x1000

static unsigned long keypad_ioremap;
static unsigned char *keypad_row_addr,*keypad_col_addr;
static unsigned short *keypad_check_addr;
char keypad_fpga_keycode[16] = {
		1,2,3,4,
		5,6,7,8,
		9,10,11,12,
		13,14,15,16
};

static int fpga_keypad12_open(struct inode * inode, struct file * file){
	printk("fpga_keypad_open, \n");
	keypad_ioremap=(unsigned long)ioremap(KEYPAD_PHY_ADDR,KEYPAD_ADDR_RANGE);

	if(!check_mem_region(keypad_ioremap, KEYPAD_ADDR_RANGE)) {
		request_mem_region(keypad_ioremap, KEYPAD_ADDR_RANGE, KEYPAD_NAME);
	}
	else {
		printk("FPGA KEYPAD Memory Alloc Faild!\n");
		return -1;
	}
	keypad_col_addr = (unsigned char *)(keypad_ioremap+0x70);
	keypad_row_addr = (unsigned char *)(keypad_ioremap+0x72);
	return 0;
}

static int fpga_keypad12_release(struct inode * inode, struct file * file){
	printk("fpga_keypad_release, \n");
	iounmap((unsigned long*)keypad_ioremap);
	release_region(keypad_ioremap, KEYPAD_ADDR_RANGE);
	return 0;
}

static ssize_t fpga_keypad12_read(struct file * file, char * buf, size_t length, loff_t * ofs){
	printk("fpga_keypad_read, \n");
	int j=1,k,i,ret;
	unsigned char funtion_key = 0;
	unsigned short value = 0;
	unsigned char tmp[4] = {0x01, 0x02, 0x04, 0x08};
	char send_buf[3] = {0};

	while(1) {
		for(i=0;i<4;i++) {
			*keypad_row_addr = tmp[i];
			value = *keypad_col_addr & 0x0f;
			if(value > 0) {
				for(k=0;k<4;k++) {
					if(value == tmp[k]) {
						value = j+(i*4);
						funtion_key = keypad_fpga_keycode[value-1];
						if(value != 0x00) goto stop_poll;
					}
					j++;
				}
			}
		}
	}

	stop_poll:
		if(value > 0) {
			sprintf(send_buf, "%02d", value);
			copy_to_user(buf, send_buf, 3);
		}
		else {
			*keypad_row_addr = 0x00;
		}
		return 0;

}

static ssize_t fpga_keypad12_write(struct file * file, const char * buf, size_t length, loff_t * ofs){
	printk("fpga_keypad12_write, \n");

	return 0;
}

static DEFINE_MUTEX(fpga_keypad12_mutex);
static long fpga_keypad12_ioctl(struct file * file, unsigned int cmd, unsigned long arg){
	printk("fpga_keypad12_ioctl, \n");

	switch(cmd){
	default:
		mutex_unlock(&fpga_keypad12_mutex);
		return ENOTTY;
	}

	mutex_unlock(&fpga_keypad12_mutex);
	return 0;
}

static struct file_operations fpga_keypad12_fops = {
		.owner = THIS_MODULE,
		.open = fpga_keypad12_open,
		.release = fpga_keypad12_release,
		.read = fpga_keypad12_read,
		.write = fpga_keypad12_write,
		.unlocked_ioctl = fpga_keypad12_ioctl,
};

static struct miscdevice fpga_keypad12_driver = {
		.minor = MISC_DYNAMIC_MINOR,
		.name = "fpga_keypad12",
		.fops = &fpga_keypad12_fops,
};

static int fpga_keypad12_init(void){
	printk("fpga_keypad12_init, \n");

	return misc_register(&fpga_keypad12_driver);
}

static void fpga_keypad12_exit(void){
	printk("fpga_keypad12_exit, \n");

	misc_deregister(&fpga_keypad12_driver);

}

module_init(fpga_keypad12_init);
module_exit(fpga_keypad12_exit);

MODULE_AUTHOR("Author of the fpga_keypad12 to put it he");
MODULE_DESCRIPTION("Description of the fpga_keypad12 to put it he");
MODULE_LICENSE("Dual BSD/GPL");
