// Quarks Folder Setter
+Quarks {
  *updateFolderPath { |path|
    folder = path.standardizePath;
  }
}

// Array2D Slices
+Array2D {

  // Modeled after the Matrix in Mathlib
  // Returns a SubArray Slice Based on the Given Shape
  // rowStart = Row Index to Begin copying
  // colStart = Column Index to Begin copying
  // numRows = number of rows to copy
  // numColumns = number of columns to copy
  getSub { | rowStart = 0, colStart = 0, shape |
    var numRows = shape[0], numColumns = shape[1];
    var copy = Array2D.new(numRows,numColumns);
    Array.fill2D(numRows, numColumns, { arg r, c;
        copy.put(r,c,this.rowAt(rowStart + r)[colStart + c]);
    });
    ^copy;
  }
}
