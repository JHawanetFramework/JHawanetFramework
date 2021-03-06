package controller;

import controller.util.ControllerUtils;
import controller.util.solutionattribute.Generation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import model.epanet.element.Network;
import model.epanet.io.OutputInpWriter;
import model.metaheuristic.problem.Problem;
import model.metaheuristic.solution.Solution;
import model.metaheuristic.util.io.SolutionListOutput;
import model.metaheuristic.util.solutionattribute.OverallConstraintViolation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.utils.CustomDialogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is the controller of result window. <br>
 * <br>
 * From this class is saved as inp the solution and saved the element has FUN
 * and VAR.
 *
 * @author gsanh
 */
public class ResultController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultController.class);

    private final Pane root;

    @FXML
    private TableView<SolutionWrap> resultTable;

    @NotNull
    private final List<? extends Solution<?>> solutionList;

    @NotNull
    private final Problem<?> problem;
    @Nullable
    private final Map<String, String> parameters;

    @NotNull
    private final Network network;

    @NotNull
    final BooleanProperty hasSelectedItem;
    @NotNull
    private final String problemName;

    /**
     * Constructor.
     *
     * @param problemName the name of the problem
     * @param solutions   a list with solution to show
     * @param problem     the problem that was solve by the algorithm.
     * @param network     the network object
     * @param parameters  the parameters which was used to configure the experiment.
     *                    It are extra colummn in Table. If it is a empty map or a null value, so the Table
     *                    only show the objectives and variables.
     * @throws NullPointerException if problemName, solutions, problem or
     *                              network is null.
     */
    public ResultController(@NotNull String problemName, @NotNull List<? extends Solution<?>> solutions, @NotNull Problem<?> problem, @NotNull Network network, @Nullable Map<String, String> parameters) {
        LOGGER.debug("Initializing ResultController class.");

        this.problemName = Objects.requireNonNull(problemName);
        this.solutionList = Objects.requireNonNull(solutions);
        this.problem = Objects.requireNonNull(problem);
        this.network = Objects.requireNonNull(network);


        this.parameters = parameters;

        this.root = ControllerUtils.loadFXML("/view/Result.fxml", this);
        this.hasSelectedItem = new SimpleBooleanProperty();

        configureResultTable();
        addBinding();
    }

    /**
     * Configure the table view adding the needed columns and the element that will be showned.
     */
    private void configureResultTable() {
        LOGGER.info("Setting up table of results.");

        if (!this.solutionList.isEmpty()) {
            Generation<Solution<?>> generationAttribute = new Generation<>();
            OverallConstraintViolation<Solution<?>> overallConstraintViolationAttribute = new OverallConstraintViolation<>();

            int numberOfObjectives = this.solutionList.get(0).getNumberOfObjectives();
            int numberOfDecisionVariables = this.solutionList.get(0).getNumberOfVariables();

            // add column for the objectives
            LOGGER.debug("Creating {} row of objectives.", numberOfObjectives);
            for (int i = 0; i < numberOfObjectives; i++) {
                final int index = i;
                TableColumn<SolutionWrap, String> column = new TableColumn<>("Objective " + (i + 1));
                resultTable.getColumns().add(column);
                // tell from where get the value for the column
                column.setCellValueFactory(
                        (CellDataFeatures<SolutionWrap, String> solutionData) -> new ReadOnlyObjectWrapper<>(
                                Double.toString(solutionData.getValue().solution.getObjective(index))));
            }

            // add column for the decision variables
            LOGGER.debug("Creating {} rows of variables.", numberOfDecisionVariables);
            for (int i = 0; i < numberOfDecisionVariables; i++) {
                final int index = i;
                TableColumn<SolutionWrap, String> column = new TableColumn<>("X" + (i + 1));
                resultTable.getColumns().add(column);
                // tell from where get the value for the column
                column.setCellValueFactory(
                        (CellDataFeatures<SolutionWrap, String> solutionData) -> new ReadOnlyObjectWrapper<>(
                                solutionData.getValue().solution.getVariableAsString(index)));
            }

            boolean hasGenerationColumn = false;
            boolean hasOverallConstraintViolationColumn = false;

            //test if has the OverallConstraintViolation and Generation attribute. Also, add the items to table.
            for (Solution<?> solution : this.solutionList) {
                Double constraint = overallConstraintViolationAttribute.getAttribute(solution);
                Integer generation = generationAttribute.getAttribute(solution);
                if (constraint != null) {
                    hasOverallConstraintViolationColumn = true;
                }
                if (generation != null) {
                    hasGenerationColumn = true;
                }
                this.resultTable.getItems().add(new SolutionWrap(solution, this.parameters
                        , constraint
                        , generation)
                );
            }

            LOGGER.debug("Add overall constrain column: {} and generation column: {}",
                    hasOverallConstraintViolationColumn,
                    hasGenerationColumn);
            if (hasOverallConstraintViolationColumn) {

                // add column for extra variables
                TableColumn<SolutionWrap, String> overallColumn = new TableColumn<>("Overall Constrain Violation");
                resultTable.getColumns().add(overallColumn);
                overallColumn.setCellValueFactory(
                        (CellDataFeatures<SolutionWrap, String> solutionData) -> {
                            if (solutionData.getValue().constraint != null) {
                                return new ReadOnlyObjectWrapper<>(""+
                                        (solutionData.getValue().constraint + 0.0)); //the + 0.0 fix the -0.0
                            } else {
                                return new ReadOnlyObjectWrapper<>("");
                            }
                        });
            }

            if (hasGenerationColumn) {

                TableColumn<SolutionWrap, String> generationColumn = new TableColumn<>("Number of Generation");
                resultTable.getColumns().add(generationColumn);
                generationColumn.setCellValueFactory(
                        (CellDataFeatures<SolutionWrap, String> solutionData) -> {
                            if (solutionData.getValue().generation != null) {
                                return new ReadOnlyObjectWrapper<>(
                                        solutionData.getValue().generation.toString());
                            } else {
                                return new ReadOnlyObjectWrapper<>("");
                            }

                        });
            }

            // add columns for parameters.
            if (parameters != null) {
                LOGGER.debug("Creating rows for extra parameters.");

                for (String key : parameters.keySet()) {
                    TableColumn<SolutionWrap, String> column = new TableColumn<>(key);
                    resultTable.getColumns().add(column);
                    // tell from where get the value for the column
                    column.setCellValueFactory(
                            (CellDataFeatures<SolutionWrap, String> solutionData) -> new ReadOnlyObjectWrapper<>(
                                    solutionData.getValue().parameters.get(key)));
                }
            }

            // empty column is use to patch a problem with scroll pane that slice the last column
            TableColumn<SolutionWrap, String> emptyColumn = new TableColumn<>("                               ");
            resultTable.getColumns().add(emptyColumn);

            // If the problem is monoobjective SolutionWrap fill with red the cell of best solution
            if (this.problem.getNumberOfObjectives() == 1){
                LOGGER.debug("Setting color of row with best solution to problem with {} objectives.", this.problem.getNumberOfObjectives());
                Optional<SolutionWrap> optional = this.resultTable.getItems().stream().min((o1, o2) -> Double.compare(o1.solution.getObjective(0), o2.solution.getObjective(0)));
                if (optional.isPresent()){
                    SolutionWrap best = optional.get();
                    this.resultTable.setRowFactory((TableView<SolutionWrap> param) -> new TableRow<SolutionWrap>() {
                        @Override
                        public void updateItem(SolutionWrap item, boolean empty) {
                            super.updateItem(item, empty) ;
                            if (item == null) {
                                setStyle("");
                            } else if (item == best) {
                                setStyle("-fx-background-color: #55ff2f;");
                            } else {
                                setStyle("");
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * Method to add binding to nodes
     */
    private void addBinding() {
        this.hasSelectedItem.bind(this.resultTable.getSelectionModel().selectedItemProperty().isNotNull());
    }

    /**
     * Save selected item as INP
     */
    public void saveSelectedItemAsINP() {
        LOGGER.info("Save selected solution as inp.");

        SolutionWrap solutionWrap = this.resultTable.getSelectionModel().getSelectedItem();
        Solution<?> solution = solutionWrap.solution;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("INP file", "*.inp"));
        fileChooser.setTitle("Save solution as INP");

        File file = fileChooser.showSaveDialog(this.resultTable.getScene().getWindow());
        if (file != null) {
            Network netCopy = this.problem.applySolutionToNetwork(this.network.copy(), solution);
            if (netCopy != null) {
                OutputInpWriter outputInpWriter = new OutputInpWriter();
                try {
                    LOGGER.debug("Writting inp file {}.", file.getAbsolutePath());
                    outputInpWriter.write(netCopy, file.getAbsolutePath());
                } catch (IOException e) {
                    LOGGER.error("The file can't be created.",e);
                    CustomDialogs.showExceptionDialog("Error", "Error in the creation of the inp file",
                            "The file can't be created", e);
                }
            } else {
                LOGGER.debug("Save selected item has inp is not supported for {} problem.", problem.getClass().getName());
                CustomDialogs.showDialog("Unsupported Operation",
                        "The save as inp operation is not supported by this problem",
                        "The method applySolutionToNetwork of " + problem.getName()
                                + " has returned null. To support this operation you need return a Network",
                        AlertType.WARNING);
            }

        }
    }

    /**
     * Save table. It save the solutions in Fun y Var file.
     */
    public void saveTable() {
        LOGGER.info("Save solution in csv file.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        fileChooser.setTitle("Save table");

        File file = fileChooser.showSaveDialog(this.resultTable.getScene().getWindow());
        if (file != null) {
            String funFilePath = Paths.get(file.getParent(), "FUN_" + file.getName()).toString();
            String varFilePath = Paths.get(file.getParent(), "VAR_" + file.getName()).toString();

            SolutionListOutput output = new SolutionListOutput(this.solutionList)
                    .setSeparator(",")
                    .setFunFileName(funFilePath)
                    .setVarFileName(varFilePath);
            try {
                LOGGER.debug("Writting {} and {} files.", funFilePath, varFilePath);

                output.write();
            } catch (IOException e) {
                LOGGER.error("The file can't be created.",e);

                CustomDialogs.showExceptionDialog("Error", "Error in the creation of fun/var file",
                        "The file can't be created", e);
            }
        }
    }

    /**
     * It write the whole table in a excel
     */
    public void saveTableAsExcel() {
        LOGGER.info("Save table in excel.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel file", "*.xlsx"));
        fileChooser.setTitle("Save table as excel");

        File file = fileChooser.showSaveDialog(this.resultTable.getScene().getWindow());

        if (file == null){
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sample");

        // add the header
        Row row = spreadsheet.createRow(0);
        for (int j = 0; j < this.resultTable.getColumns().size()-1; j++) { //remove the empty column
            row.createCell(j).setCellValue(this.resultTable.getColumns().get(j).getText());
        }


        for (int i = 0; i < this.resultTable.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < this.resultTable.getColumns().size()-1; j++) { //remove the empty column
                if (this.resultTable.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(this.resultTable.getColumns().get(j).getCellData(i).toString());
                } else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath())) {
            LOGGER.debug("Writting excel file {}.", file.getAbsolutePath());
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            LOGGER.error("The file can't be created because the file exists but is a directory rather than a regular file"
                    + "does not exist but cannot be created, or cannot be opened for any other reason.", e);

            CustomDialogs.showDialog("Error", "Error in the creation of the excel file",
                    "The file can't be created because the file exists but is a directory rather than a regular file," +
                            " does not exist but cannot be created, or cannot be opened for any other reason", AlertType.ERROR);
        } catch (IOException e) {
            LOGGER.error("The excel can't be written.", e);

            CustomDialogs.showExceptionDialog("Error", "Error in the creation of the excel file",
                    "The file can't be created", e);
        }
    }

    /**
     * Get the graphic node.
     * @return the graphic node.
     */
    public Node getNode() {
        return root;
    }

    public @NotNull String getProblemName() {
        return problemName;
    }

    /**
     * It indicate if there is a selected element in Table.
     *
     * @return true if there is a selected item or false in other wise.
     */
    public boolean hasSelectedItem() {
        return hasSelectedItem.get();
    }

    /**
     * It is the property that indicate if there is a selected element in Table.
     *
     * @return a property
     */
    public @NotNull BooleanProperty hasSelectedItemProperty() {
        return hasSelectedItem;
    }

    /**
     * This class is used as a wrap for solution. It let add more info to Table.
     */
    private static final class SolutionWrap {
        private final @NotNull Solution<?> solution;
        private final @Nullable Map<String, String> parameters;
        private final @Nullable Double constraint;
        private final @Nullable Integer generation;


        SolutionWrap(@NotNull Solution<?> solution, @Nullable Map<String, String> parameters, @Nullable Double constraint, @Nullable Integer generation) {
            this.solution = solution;
            this.parameters = parameters;
            this.constraint = constraint;
            this.generation = generation;
        }
    }
}
