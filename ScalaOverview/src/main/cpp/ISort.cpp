#include <iostream>
using namespace std;

// Forward declarations of functions defined later
void isort(int a[], int N);
void display(int a[], int N);

int main()
{
  int data[] = {3, 1, 4, 1, 5, 9, 2, 6, 5};
  const int size = sizeof(data) / sizeof(int);

  isort(data, size);
  display(data, size);
}

void isort(int a[], int N)
{
  for (int i = 1; i < N; ++i) {
    int value = a[i];
    int j = i;
    while (j > 0 && a[j-1] > value) {
      a[j] = a[j-1];
      --j;
    }
    a[j] = value;
  }
}

void display(int a[], int N)
{
  for (int i = 0; i < N; ++i) {
    cout << a[i] << " ";
  }
  cout << endl;
}
