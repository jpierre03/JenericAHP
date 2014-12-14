PRG:=ahp.out

all: $(PRG) run

clean:
	rm $(PRG)

$(PRG): sandbox.c
	cc $< -o $(PRG)

run:	$(PRG)
	./$(PRG)

