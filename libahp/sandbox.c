#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef struct {
	unsigned short int size;
	double* weight;
} globalPriority;

globalPriority* buildGlobalPriorityVector(){
	unsigned short int size = 5;
	double weight[] = {0.2, 0.2, 0.1, 0.4, 0.2};

	globalPriority* gp = malloc(sizeof(globalPriority));
	gp->size = size;
	gp->weight = malloc(size * sizeof(double));
	memcpy(gp->weight, weight, size * sizeof(double)); 

	return gp;
}

void displayGlobalPriority(globalPriority* gp){
	int i = 0;
	for(i = 0; i < gp->size; i++){
		printf("%d %ld\n", i, (gp->weight)[i]);
	}

}


int main(int argc, char** argv){
	printf("Hello world !\n");
	globalPriority* gp = buildGlobalPriorityVector();
	displayGlobalPriority(gp);

	return 0;
}
