f = open("./uc.json").read().split("\n")

for i in f:
    if "birthmark" in i:
        print i.replace(".", "-")
    else:
        print i
