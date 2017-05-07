extractor = bmsys.extractor(argv[1]);
source = fs.open(argv[2]);
birthmarks = extractor.extract(source);

// var rs = WScript.CreateObject("Scripting.FileSystemObject");
// var file = rs.CreateTextFile("text.txt");
// file.Write(birthmarks);
// file.close();
//
// fs.createFile("test.txt");
// fs.writeFile("test.txt", birthmarks);
//
fs.writer(argv[3], birthmarks)

// fs.print(birthmarks);
//
// fs.print("extraction: " + birthmarks.time() + " ns")
//

