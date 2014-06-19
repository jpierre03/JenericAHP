PRG:=ahp.out

all: $(PRG) run

$(PRG): sandbox.c
	cc $< -o $(PRG)

run:	$(PRG)
	./$(PRG)

