#include <iostream>
using namespace std;

int main(int argc, char *argv[])
{
  int i = 1; // skip argv[0], which is the program name
  while (i < argc) {
    cout << "Hello, " << argv[i] << endl;
    ++i;
  }
}
